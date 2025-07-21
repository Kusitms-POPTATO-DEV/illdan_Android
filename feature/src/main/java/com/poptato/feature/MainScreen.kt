package com.poptato.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.poptato.core.enums.BottomNavType
import com.poptato.design_system.BottomSheet
import com.poptato.design_system.SNACK_BAR_FINISH_APP_GUIDE
import com.poptato.design_system.Gray100
import com.poptato.design_system.R
import com.poptato.domain.model.enums.BottomSheetType
import com.poptato.domain.model.enums.DialogType
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.model.response.dialog.DialogContentModel
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
import com.poptato.ui.common.OneBtnTypeDialog
import com.poptato.ui.common.RoutineBottomSheet
import com.poptato.ui.common.TimePickerBottomSheet
import com.poptato.ui.common.TodoBottomSheet
import com.poptato.ui.common.TwoBtnTypeDialog
import com.poptato.ui.event.TodoExternalEvent
import com.poptato.ui.util.CommonEventManager
import com.poptato.ui.util.DismissKeyboardOnClick
import com.poptato.ui.viewModel.GuideViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val guideViewModel: GuideViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val showSecondGuide by guideViewModel.showSecondGuide.collectAsState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }
    val interactionSource = remember { MutableInteractionSource() }
    val isShowDialog = remember { mutableStateOf(false) }
    val showBottomSheet: (TodoItemModel, List<CategoryItemModel>) -> Unit =
        { item: TodoItemModel, categoryList: List<CategoryItemModel> ->
            viewModel.onSelectedTodoItem(item, categoryList)
        }
    val showCategoryIconBottomSheet: (CategoryIconTotalListModel) -> Unit =
        { categoryList: CategoryIconTotalListModel ->
            viewModel.onSelectedCategoryIcon(categoryList)
        }
    val backPressHandler: () -> Unit = {
        scope.launch { viewModel.todoEventFlow.emit(TodoExternalEvent.ActiveItem(-1L)) }
        if (uiState.bottomSheetType != BottomSheetType.None) {
            viewModel.updateBottomSheetType(BottomSheetType.None)
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
            viewModel.categoryScreenContentFromBacklog.emit(it)
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

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    // 백그라운드 -> 포그라운드 전환 시 어제 한 일 API 호출
                    viewModel.getYesterdayList()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.isExistYesterday) {
        if (uiState.isExistYesterday) {
            navController.navigate(NavRoutes.YesterdayListScreen.route)
        }
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

    DismissKeyboardOnClick(
        callback = { scope.launch { viewModel.todoEventFlow.emit(TodoExternalEvent.ActiveItem(-1L)) } }
    ) {
        if (isShowDialog.value) {
            when (uiState.dialogContent.dialogType) {
                DialogType.OneBtn -> { OneBtnTypeDialog(onDismiss = { isShowDialog.value = false }, dialogContent = uiState.dialogContent) }
                DialogType.TwoBtn -> { TwoBtnTypeDialog(onDismiss = { isShowDialog.value = false }, dialogContent = uiState.dialogContent) }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray100)
                .navigationBarsPadding()
        ) {
            Scaffold(
                bottomBar = {
                    BottomBarContent(
                        uiState = uiState,
                        showSecondGuide = showSecondGuide,
                        navController = navController,
                        interactionSource = interactionSource,
                        getYesterdayList = { viewModel.getYesterdayList() },
                        logAnalyticsEventForRoute = { viewModel.logAnalyticsEventForRoute(it) },
                        updateSecondGuide = {
                            guideViewModel.updateSecondGuide(it)
                            guideViewModel.updateThirdGuide(true)
                        }
                    )
                },
                snackbarHost = { CommonSnackBar(hostState = snackBarHost) },
            ) { innerPadding ->
                NavHostContent(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    showSecondGuide = showSecondGuide,
                    userDeleteName = viewModel.userDeleteName,
                    todoEventFlow = viewModel.todoEventFlow,
                    selectedIconInBottomSheet = viewModel.selectedIconInBottomSheet,
                    categoryScreenContentFromBacklog = viewModel.categoryScreenContentFromBacklog,
                    deleteUserName = deleteUserName,
                    categoryScreenContent = categoryScreenContent,
                    showSnackBar = showSnackBar,
                    showBottomSheet = showBottomSheet,
                    showDialog = showDialog,
                    showCategoryIconBottomSheet = showCategoryIconBottomSheet
                )
            }

            if (uiState.bottomSheetType != BottomSheetType.None) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BottomSheet)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { viewModel.updateBottomSheetType(BottomSheetType.None) }
                        )
                )
            }

            BottomSheetContent(
                uiState = uiState,
                onClickShowDatePicker = { viewModel.updateBottomSheetType(BottomSheetType.FullDate) },
                onClickBtnDelete = { viewModel.onDeleteTodo(it) },
                onClickBtnModify = { viewModel.onModifyTodo(it) },
                onClickBtnBookmark = { viewModel.onUpdateTodoBookmark(it, !uiState.selectedTodoItem.isBookmark) },
                onClickCategoryBottomSheet = { viewModel.updateBottomSheetType(BottomSheetType.CategoryList) },
                onClickBtnTime = { viewModel.updateBottomSheetType(BottomSheetType.TimePicker) },
                onClickBtnRoutine = { viewModel.updateBottomSheetType(BottomSheetType.Routine) },
                onClickUpdateDeadline = { viewModel.onUpdatedTodoDeadline(it) },
                onClickUpdateTodoTime = { viewModel.onUpdateTodoTime(it.first, it.second) },
                onClickUpdateTodoRoutine = { id, days -> viewModel.onUpdateTodoRoutine(id, days) },
                onClickUpdateTodoRepeat = { id, value -> viewModel.onUpdateTodoRepeat(id, value) },
                onCategoryIconSelected = { viewModel.onCategoryIconSelected(it) },
                onCategorySelected = { viewModel.onCategorySelected(it) },
                onDismissRequest = { viewModel.updateBottomSheetType(it) },
                backPressHandler = backPressHandler
            )
        }
    }
}

@Composable
fun BottomSheetContent(
    uiState: MainPageState,
    // Callback
    onClickShowDatePicker: () -> Unit,
    onClickBtnDelete: (Long) -> Unit,
    onClickBtnModify: (Long) -> Unit,
    onClickBtnBookmark: (Long) -> Unit,
    onClickCategoryBottomSheet: () -> Unit,
    onClickBtnTime: () -> Unit,
    onClickBtnRoutine: () -> Unit,
    onClickUpdateDeadline: (String?) -> Unit,
    onClickUpdateTodoTime: (Pair<Long, Triple<String, Int, Int>?>) -> Unit,
    onClickUpdateTodoRoutine: (Long, Set<Int>?) -> Unit,
    onClickUpdateTodoRepeat: (Long, Boolean) -> Unit,
    onCategoryIconSelected: (CategoryIconItemModel) -> Unit,
    onCategorySelected: (Long?) -> Unit,
    onDismissRequest: (BottomSheetType) -> Unit,
    backPressHandler: () -> Unit,
) {
    val currentType = uiState.bottomSheetType
    var previousType by remember { mutableStateOf(BottomSheetType.None) }
    val isSwitchingBetweenSheets = previousType != BottomSheetType.None &&
            currentType != BottomSheetType.None &&
            previousType != currentType

    LaunchedEffect(currentType) {
        previousType = currentType
    }

    AnimatedVisibility(
        visible = currentType != BottomSheetType.None,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedContent(
                targetState = uiState.bottomSheetType,
                transitionSpec = {
                    if (isSwitchingBetweenSheets) { (fadeIn(tween(250))).togetherWith(fadeOut(tween(250))) }
                    else { (fadeIn(tween(300))).togetherWith(fadeOut(tween(300))) }
                },
                label = "BottomSheetSwitcher"
            ) { targetType ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Gray100, shape = RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .align(Alignment.BottomCenter)
                ) {
                    when (targetType) {
                        BottomSheetType.Main -> {
                            TodoBottomSheet(
                                item = uiState.selectedTodoItem,
                                categoryItem = uiState.selectedTodoCategoryItem,
                                onClickShowDatePicker = onClickShowDatePicker,
                                onClickBtnDelete = onClickBtnDelete,
                                onClickBtnModify = onClickBtnModify,
                                onClickBtnBookmark = onClickBtnBookmark,
                                onClickCategoryBottomSheet = onClickCategoryBottomSheet,
                                onClickBtnTime = onClickBtnTime,
                                onClickBtnRoutine = onClickBtnRoutine
                            )
                        }
                        BottomSheetType.FullDate -> {
                            CalendarBottomSheet(
                                onDismissRequest = { onDismissRequest(BottomSheetType.Main) },
                                onDateSelected = onClickUpdateDeadline,
                                deadline = uiState.selectedTodoItem.deadline
                            )
                        }
                        // 카테고리 생성 화면 카테고리 리스트 바텀시트
                        BottomSheetType.CategoryIcon -> {
                            CategoryIconBottomSheet(
                                categoryIconList = uiState.categoryIconList,
                                onSelectCategoryIcon = onCategoryIconSelected,
                                onClickBackButton = backPressHandler
                            )
                        }
                        // 할 일 카테고리 설정 바텀시트
                        BottomSheetType.CategoryList -> {
                            CategoryBottomSheet(
                                categoryId = uiState.selectedTodoCategoryItem?.categoryId ?: -1,
                                categoryList = uiState.categoryList,
                                onDismiss = { onDismissRequest(BottomSheetType.Main) },
                                onCategorySelected = onCategorySelected
                            )
                        }
                        // 할 일 시간 설정 바텀시트
                        BottomSheetType.TimePicker -> {
                            TimePickerBottomSheet(
                                item = uiState.selectedTodoItem,
                                onDismissRequest = { onDismissRequest(BottomSheetType.Main) },
                                onClickCompletionButton = onClickUpdateTodoTime
                            )
                        }
                        BottomSheetType.Routine -> {
                            RoutineBottomSheet(
                                item = uiState.selectedTodoItem,
                                onDismissRequest = { onDismissRequest(BottomSheetType.Main) },
                                updateTodoRepeat = onClickUpdateTodoRepeat,
                                updateTodoRoutine = onClickUpdateTodoRoutine
                            )
                        }
                        BottomSheetType.None -> { Spacer(modifier = Modifier.height(0.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarContent(
    // State
    uiState: MainPageState,
    showSecondGuide: Boolean,
    interactionSource: MutableInteractionSource,
    // NavController
    navController: NavHostController,
    // Callback
    getYesterdayList: () -> Unit,
    logAnalyticsEventForRoute: (String) -> Unit,
    updateSecondGuide: (Boolean) -> Unit
) {
    Box(modifier = Modifier.background(Gray100)) {
        AnimatedVisibility(
            visible = uiState.bottomNavType != BottomNavType.DEFAULT,
            modifier = Modifier.background(Gray100)
        ) {
            BottomNavBar(
                type = uiState.bottomNavType,
                onClick = { route: String ->
                    if (navController.currentDestination?.route != route) {
                        getYesterdayList()
                        if (route == NavRoutes.BacklogScreen.route) {
                            navController.navigate(NavRoutes.BacklogScreen.createRoute(0)) {
                                popUpTo(navController.currentDestination?.route!!) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            if (route == NavRoutes.TodayScreen.route && showSecondGuide) { updateSecondGuide(false) }
                            navController.navigate(route) {
                                popUpTo(navController.currentDestination?.route!!) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        logAnalyticsEventForRoute(route)
                    }
                },
                modifier = Modifier.navigationBarsPadding().background(Gray100),
                interactionSource = interactionSource
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavHostContent(
    // modifier
    modifier: Modifier = Modifier,
    // navController
    navController: NavHostController,
    // State
    showSecondGuide: Boolean = false,
    // Flow
    userDeleteName: SharedFlow<String>,
    todoEventFlow: SharedFlow<TodoExternalEvent>,
    selectedIconInBottomSheet: SharedFlow<CategoryIconItemModel>,
    categoryScreenContentFromBacklog: SharedFlow<CategoryScreenContentModel>,
    // Callback
    deleteUserName: (String) -> Unit = {},
    categoryScreenContent: (CategoryScreenContentModel) -> Unit = {},
    showSnackBar: (String) -> Unit = {},
    showBottomSheet: (TodoItemModel, List<CategoryItemModel>) -> Unit = { _, _ -> },
    showDialog: (DialogContentModel) -> Unit = {},
    showCategoryIconBottomSheet: (CategoryIconTotalListModel) -> Unit = {},
) {
    val animationDuration = 300

    Box(
        modifier = modifier
    ) {
        NavHost(
            modifier = Modifier.background(Gray100),
            navController = navController,
            startDestination = NavRoutes.SplashGraph.route,
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = animationDuration, easing = LinearOutSlowInEasing)) },
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis = animationDuration, easing = LinearOutSlowInEasing)) }
        ) {
            splashNavGraph(navController = navController)
            loginNavGraph(navController = navController, showSnackBar = showSnackBar)
            yesterdayListNavGraph(navController = navController)
            myPageNavGraph(
                navController = navController,
                showDialog = showDialog,
                showSnackBar = showSnackBar,
                deleteUserName = deleteUserName,
                deleteUserNameFromUserData = userDeleteName
            )
            backlogNavGraph(
                navController = navController,
                showBottomSheet = showBottomSheet,
                todoExternalEvent = todoEventFlow,
                showSnackBar = showSnackBar,
                showDialog = showDialog,
                categoryScreenContent = categoryScreenContent
            )
            todayNavGraph(
                navController = navController,
                showSnackBar = showSnackBar,
                showBottomSheet = showBottomSheet,
                todoExternalEvent = todoEventFlow
            )
            categoryNavGraph(
                navController = navController,
                showCategoryIconBottomSheet = showCategoryIconBottomSheet,
                selectedIconInBottomSheet = selectedIconInBottomSheet,
                showDialog = showDialog,
                categoryScreenFromBacklog = categoryScreenContentFromBacklog
            )
            historyNavGraph(
                navController = navController
            )
        }

        if (showSecondGuide) {
            Icon(
                painter = painterResource(id = R.drawable.ic_guide_bubble_2),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 20.dp)
            )
        }
    }
}