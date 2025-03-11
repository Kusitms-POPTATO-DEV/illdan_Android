package com.poptato.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.poptato.core.enums.BottomNavType
import com.poptato.core.util.TimeFormatter
import com.poptato.design_system.SNACK_BAR_FINISH_APP_GUIDE
import com.poptato.design_system.Gray100
import com.poptato.domain.model.enums.BottomSheetType
import com.poptato.domain.model.enums.DialogType
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.model.response.dialog.DialogContentModel
import com.poptato.domain.model.response.history.CalendarMonthModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.feature.component.BottomNavBar
import com.poptato.navigation.NavRoutes
import com.poptato.navigation.backlogNavGraph
import com.poptato.navigation.categoryNavGraph
import com.poptato.navigation.historyNavGraph
import com.poptato.navigation.loginNavGraph
import com.poptato.navigation.myPageNavGraph
import com.poptato.navigation.splashNavGraph
import com.poptato.navigation.todayNavGraph
import com.poptato.navigation.yesterdayListNavGraph
import com.poptato.ui.common.CalendarBottomSheet
import com.poptato.ui.common.CategoryBottomSheet
import com.poptato.ui.common.CategoryIconBottomSheet
import com.poptato.ui.common.CommonSnackBar
import com.poptato.ui.common.DatePickerBottomSheet
import com.poptato.ui.common.MonthPickerBottomSheet
import com.poptato.ui.common.OneBtnTypeDialog
import com.poptato.ui.common.TodoBottomSheet
import com.poptato.ui.common.TwoBtnTypeDialog
import com.poptato.ui.util.AnalyticsManager
import com.poptato.ui.util.CommonEventManager
import com.poptato.ui.util.DismissKeyboardOnClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }
    val interactionSource = remember { MutableInteractionSource() }
    val sheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val isShowDialog = remember { mutableStateOf(false) }
    val showBottomSheet: (TodoItemModel, List<CategoryItemModel>) -> Unit =
        { item: TodoItemModel, categoryList: List<CategoryItemModel> ->
            viewModel.onSelectedTodoItem(item, categoryList)
            scope.launch { sheetState.show() }
        }
    val showCategoryIconBottomSheet: (CategoryIconTotalListModel) -> Unit =
        { categoryList: CategoryIconTotalListModel ->
            viewModel.onSelectedCategoryIcon(categoryList)
            scope.launch { sheetState.show() }
        }
    val showMonthPickerBottomSheet: (CalendarMonthModel) -> Unit = {
            currentMonthModel: CalendarMonthModel ->
        viewModel.showMonthPicker(currentMonthModel)
        scope.launch { sheetState.show() }
    }
    val backPressHandler: () -> Unit = {
        if (sheetState.isVisible) {
            scope.launch { sheetState.hide() }
        } else if (uiState.backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            viewModel.toggleBackPressed(true)
            Toast.makeText(context, SNACK_BAR_FINISH_APP_GUIDE, Toast.LENGTH_SHORT).show()
            scope.launch {
                delay(2000)
                viewModel.toggleBackPressed(false)
            }
        }
    }
    val showSnackBar: (String) -> Unit = { message ->
        scope.launch { snackBarHost.showSnackbar(message = message) }
    }
    val showDialog: (DialogContentModel) -> Unit = {
        viewModel.onSetDialogContent(it)
        isShowDialog.value = true
    }
    val categoryScreenContent: (CategoryScreenContentModel) -> Unit = {
        scope.launch {
            viewModel.categoryScreenContent.emit(it)
        }
    }
    val deleteUserName: (String) -> Unit = {
        scope.launch {
            viewModel.userDeleteName.emit(it)
        }
    }

    if (uiState.bottomNavType != BottomNavType.DEFAULT) {
        BackHandler(onBack = backPressHandler)
    }

    LaunchedEffect(Unit) {
        CommonEventManager.logoutTriggerFlow.collect {
            navController.navigate(NavRoutes.KaKaoLoginGraph.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow
            .distinctUntilChanged()
            .collect { backStackEntry ->
                viewModel.setBottomNavType(backStackEntry.destination.route)
            }
    }

    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.isVisible }
            .distinctUntilChanged()
            .collect { isVisible ->
                if (!isVisible) {
                    viewModel.updateBottomSheetType(BottomSheetType.Main)
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is MainEvent.ShowTodoBottomSheet -> {
                    sheetState.show()
                }
            }
        }
    }

    DismissKeyboardOnClick {
        if (isShowDialog.value) {
            when (uiState.dialogContent.dialogType) {
                DialogType.OneBtn -> {
                    OneBtnTypeDialog(
                        onDismiss = { isShowDialog.value = false },
                        dialogContent = uiState.dialogContent
                    )
                }

                DialogType.TwoBtn -> {
                    TwoBtnTypeDialog(
                        onDismiss = { isShowDialog.value = false },
                        dialogContent = uiState.dialogContent
                    )
                }
            }
        }

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                AnimatedContent(
                    targetState = uiState.bottomSheetType,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(
                            animationSpec = tween(
                                500
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (uiState.bottomSheetType == BottomSheetType.CategoryIcon) {
                                Modifier.height(610.dp)
                            } else {
                                Modifier.wrapContentHeight()
                            }
                        )
                        .background(Gray100)
                        .navigationBarsPadding(),
                    label = ""
                ) { currentSheet ->
                    when (currentSheet) {
                        BottomSheetType.Main -> {
                            TodoBottomSheet(
                                item = uiState.selectedTodoItem,
                                categoryItem = uiState.selectedTodoCategoryItem,
                                onClickShowDatePicker = {
                                    viewModel.updateBottomSheetType(
                                        BottomSheetType.FullDate
                                    )
                                },
                                onClickBtnDelete = {
                                    scope.launch {
                                        viewModel.deleteTodoFlow.emit(it)
                                        sheetState.hide()
                                    }
                                },
                                onClickBtnModify = {
                                    scope.launch {
                                        viewModel.activateItemFlow.emit(it)
                                        sheetState.hide()
                                    }
                                },
                                onClickBtnBookmark = {
                                    viewModel.onUpdatedBookmark(!uiState.selectedTodoItem.isBookmark)
                                    AnalyticsManager.logEvent(
                                        eventName = "set_important",
                                        params = mapOf("task_ID" to "${uiState.selectedTodoItem.todoId}")
                                    )
                                    scope.launch {
                                        viewModel.updateBookmarkFlow.emit(it)
                                    }
                                },
                                onClickCategoryBottomSheet = {
                                    viewModel.updateBottomSheetType(BottomSheetType.CategoryList)
                                },
                                onClickBtnRepeat = {
                                    viewModel.onUpdatedTodoRepeat(!uiState.selectedTodoItem.isRepeat)
                                    scope.launch { viewModel.updateTodoRepeatFlow.emit(it) }
                                }
                            )
                        }

                        BottomSheetType.FullDate -> {
                            CalendarBottomSheet(
                                onDismissRequest = { viewModel.updateBottomSheetType(BottomSheetType.Main) },
                                onDateSelected = { date ->
                                    viewModel.onUpdatedDeadline(date)
                                    AnalyticsManager.logEvent(
                                        eventName = "set_dday",
                                        params = mapOf("set_date" to TimeFormatter.getTodayFullDate(), "dday" to "$date", "task_ID" to "${uiState.selectedTodoItem.todoId}")
                                    )
                                    scope.launch { viewModel.updateDeadlineFlow.emit(date) }
                                },
                                deadline = uiState.selectedTodoItem.deadline
                            )
                        }

                        BottomSheetType.Calendar -> TODO("캘린더 바텀시트 컴포저블을 여기에 추가")
                        BottomSheetType.SubDate -> {
                            DatePickerBottomSheet(
                                onDismissRequest = { viewModel.updateBottomSheetType(BottomSheetType.Calendar) },
                                bottomSheetType = BottomSheetType.SubDate
                            )
                        }

                        BottomSheetType.CategoryIcon -> {
                            CategoryIconBottomSheet(
                                categoryIconList = uiState.categoryIconList,
                                onSelectCategoryIcon = {
                                    scope.launch {
                                        viewModel.selectedIconInBottomSheet.emit(it)
                                        sheetState.hide()
                                    }
                                }
                            )
                        }

                        BottomSheetType.CategoryList -> {
                            CategoryBottomSheet(
                                categoryId = uiState.selectedTodoCategoryItem?.categoryId ?: -1,
                                categoryList = uiState.categoryList,
                                onDismiss = {
                                    viewModel.updateBottomSheetType(BottomSheetType.Main)
                                },
                                onCategorySelected = {
                                    viewModel.onUpdatedCategory(it)
                                    AnalyticsManager.logEvent(
                                        eventName = "set_category"
                                    )
                                    scope.launch { viewModel.updateCategoryFlow.emit(it) }
                                }
                            )
                        }
                        BottomSheetType.MonthPicker -> {
                            MonthPickerBottomSheet(
                                initialMonthModel = uiState.selectedMonth,
                                onYearMonthSelected = { selectedModel ->
                                    viewModel.onMonthSelected(selectedModel)
                                    scope.launch {
                                        viewModel.updateMonthFlow.emit(selectedModel)
                                    }
                                    viewModel.updateBottomSheetType(BottomSheetType.Main)
                                    scope.launch { sheetState.hide() }
                                },
                                onDismissRequest = {
                                    viewModel.updateBottomSheetType(BottomSheetType.Main)
                                    scope.launch { sheetState.hide() }
                                }
                            )
                        }
                    }
                }
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            scrimColor = Color(0, 0, 0, 128)
        ) {
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = uiState.bottomNavType != BottomNavType.DEFAULT,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = viewModel.animationDuration,
                                easing = FastOutSlowInEasing
                            )
                        ) + scaleIn(
                            initialScale = 0.9f,
                            animationSpec = tween(
                                durationMillis = viewModel.animationDuration,
                                easing = FastOutSlowInEasing
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = viewModel.animationDuration,
                                easing = LinearOutSlowInEasing
                            )
                        ) + scaleOut(
                            targetScale = 0.9f,
                            animationSpec = tween(
                                durationMillis = viewModel.animationDuration,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                        modifier = Modifier.background(Gray100)
                    ) {
                        BottomNavBar(
                            type = uiState.bottomNavType,
                            onClick = { route: String ->
                                if (navController.currentDestination?.route != route) {
                                    navController.navigate(route) {
                                        popUpTo(navController.currentDestination?.route!!) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            modifier = Modifier.navigationBarsPadding(),
                            interactionSource = interactionSource
                        )
                    }
                },
                snackbarHost = { CommonSnackBar(hostState = snackBarHost) },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    NavHost(
                        modifier = Modifier.background(Gray100),
                        navController = navController,
                        startDestination = NavRoutes.SplashGraph.route,
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    durationMillis = viewModel.animationDuration,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                        },
                        enterTransition = {
                            fadeIn(
                                animationSpec = tween(
                                    durationMillis = viewModel.animationDuration,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        },
                        popEnterTransition = {
                            fadeIn(
                                animationSpec = tween(
                                    durationMillis = viewModel.animationDuration,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        },
                        popExitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    durationMillis = viewModel.animationDuration,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                        }
                    ) {
                        splashNavGraph(navController = navController)
                        loginNavGraph(navController = navController, showSnackBar = showSnackBar)
                        yesterdayListNavGraph(navController = navController)
                        myPageNavGraph(
                            navController = navController,
                            showDialog = showDialog,
                            deleteUserName = deleteUserName,
                            deleteUserNameFromUserData = viewModel.userDeleteName
                        )
                        backlogNavGraph(
                            navController = navController,
                            showBottomSheet = showBottomSheet,
                            updateDeadlineFlow = viewModel.updateDeadlineFlow,
                            deleteTodoFlow = viewModel.deleteTodoFlow,
                            activateItemFlow = viewModel.activateItemFlow,
                            updateBookmarkFlow = viewModel.updateBookmarkFlow,
                            updateCategoryFlow = viewModel.updateCategoryFlow,
                            showSnackBar = showSnackBar,
                            showDialog = showDialog,
                            categoryScreenContent = categoryScreenContent,
                            updateTodoRepeatFlow = viewModel.updateTodoRepeatFlow
                        )
                        todayNavGraph(
                            navController = navController,
                            showSnackBar = showSnackBar,
                            showBottomSheet = showBottomSheet,
                            updateDeadlineFlow = viewModel.updateDeadlineFlow,
                            updateBookmarkFlow = viewModel.updateBookmarkFlow,
                            deleteTodoFlow = viewModel.deleteTodoFlow,
                            activateItemFlow = viewModel.activateItemFlow,
                            updateCategoryFlow = viewModel.updateCategoryFlow,
                            updateTodoRepeatFlow = viewModel.updateTodoRepeatFlow
                        )
                        categoryNavGraph(
                            navController = navController,
                            showCategoryIconBottomSheet = showCategoryIconBottomSheet,
                            selectedIconInBottomSheet = viewModel.selectedIconInBottomSheet,
                            showDialog = showDialog,
                            categoryScreenFromBacklog = viewModel.categoryScreenContent
                        )
                        historyNavGraph(
                            navController = navController,
                            showBottomSheet = showMonthPickerBottomSheet,
                            updateMonthFlow = viewModel.updateMonthFlow)
                    }
                }
            }
        }
    }
}