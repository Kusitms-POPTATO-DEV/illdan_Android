package com.poptato.backlog

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.poptato.design_system.ALL
import com.poptato.design_system.BACKLOG_YESTERDAY_TASK_GUIDE
import com.poptato.design_system.BacklogHint
import com.poptato.design_system.CONFIRM_ACTION
import com.poptato.design_system.Cancel
import com.poptato.design_system.CategoryDeleteDropDownContent
import com.poptato.design_system.CategoryDeleteDropDownTitle
import com.poptato.design_system.DELETE
import com.poptato.design_system.DELETE_ACTION
import com.poptato.design_system.Danger50
import com.poptato.design_system.ERROR_GENERIC_MESSAGE
import com.poptato.design_system.EmptyBacklogTitle
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray30
import com.poptato.design_system.Gray60
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray80
import com.poptato.design_system.Gray90
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.R
import com.poptato.design_system.SNACK_BAR_COMPLETE_DELETE_TODO
import com.poptato.design_system.modify
import com.poptato.domain.model.enums.CategoryScreenType
import com.poptato.domain.model.enums.DialogType
import com.poptato.domain.model.request.todo.ModifyTodoRequestModel
import com.poptato.domain.model.request.todo.TodoContentModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.model.response.dialog.DialogContentModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.common.BookmarkItem
import com.poptato.ui.common.RepeatItem
import com.poptato.ui.common.TopBar
import com.poptato.ui.common.formatDeadline
import com.poptato.ui.util.AnalyticsManager
import com.poptato.ui.util.LoadingManager
import com.poptato.ui.util.rememberDragDropListState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BacklogScreen(
    goToYesterdayList: () -> Unit = {},
    goToCategorySelect: (CategoryScreenContentModel) -> Unit = {},
    showBottomSheet: (TodoItemModel, List<CategoryItemModel>) -> Unit = { _, _ -> },
    updateDeadlineFlow: SharedFlow<String?>,
    deleteTodoFlow: SharedFlow<Long>,
    activateItemFlow: SharedFlow<Long>,
    updateBookmarkFlow: SharedFlow<Long>,
    updateCategoryFlow: SharedFlow<Long?>,
    updateTodoRepeatFlow: SharedFlow<Long>,
    showSnackBar: (String) -> Unit,
    showDialog: (DialogContentModel) -> Unit = {}
) {
    val viewModel: BacklogViewModel = hiltViewModel()
    val interactionSource = remember { MutableInteractionSource() }
    val uiState: BacklogPageState by viewModel.uiState.collectAsStateWithLifecycle()
    var activeItemId by remember { mutableStateOf<Long?>(null) }
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(activateItemFlow) {
        activateItemFlow.collect { id ->
            activeItemId = id
        }
    }

    LaunchedEffect(deleteTodoFlow) {
        deleteTodoFlow.collect {
            viewModel.deleteBacklog(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is BacklogEvent.OnFailedUpdateBacklogList -> {
                    showSnackBar(ERROR_GENERIC_MESSAGE)
                }

                is BacklogEvent.OnSuccessDeleteBacklog -> {
                    showSnackBar(SNACK_BAR_COMPLETE_DELETE_TODO)
                }
            }
        }
    }

    LaunchedEffect(updateDeadlineFlow) {
        updateDeadlineFlow.collect {
            viewModel.setDeadline(it, uiState.selectedItem.todoId)
        }
    }

    LaunchedEffect(updateBookmarkFlow) {
        updateBookmarkFlow.collect {
            viewModel.updateBookmark(it)
        }
    }

    LaunchedEffect(updateCategoryFlow) {
        updateCategoryFlow.collect {
            viewModel.updateCategory(uiState.selectedItem.todoId, it)
        }
    }

    LaunchedEffect(updateTodoRepeatFlow) {
        updateTodoRepeatFlow.collect {
            viewModel.updateTodoRepeat(it)
        }
    }

    LaunchedEffect(uiState.isFinishedInitialization) {
        if (uiState.isFinishedInitialization) {
            LoadingManager.endLoading()
        }
    }

    if (uiState.isFinishedInitialization) {
        BacklogContent(
            uiState = uiState,
            onValueChange = { newValue -> viewModel.onValueChange(newValue) },
            createBacklog = { newItem -> viewModel.createBacklog(newItem) },
            onItemSwiped = { itemToRemove -> viewModel.swipeBacklogItem(itemToRemove) },
            onClickYesterdayList = { goToYesterdayList() },
            onSelectCategory = { index ->
                viewModel.getBacklogListInCategory(index)
            },
            onClickCategoryAdd = {
                goToCategorySelect(
                    CategoryScreenContentModel(
                        CategoryScreenType.Add
                    )
                )
            },
            onClickCategoryDeleteDropdown = {
                showDialog(
                    DialogContentModel(
                        dialogType = DialogType.TwoBtn,
                        titleText = CategoryDeleteDropDownTitle,
                        dialogContentText = CategoryDeleteDropDownContent,
                        positiveBtnText = DELETE,
                        cancelBtnText = Cancel,
                        positiveBtnAction = {
                            isDropDownMenuExpanded = false
                            viewModel.deleteCategory()
                        }
                    )
                )
            },
            onClickCategoryModifyDropdown = {
                isDropDownMenuExpanded = false
                goToCategorySelect(
                    CategoryScreenContentModel(
                        CategoryScreenType.Modify,
                        uiState.categoryList[uiState.selectedCategoryIndex]
                    )
                )
            },
            onClickBtnTodoSettings = {
                viewModel.getSelectedItemDetailContent(uiState.backlogList[it]) { callback ->
                    showBottomSheet(callback, uiState.categoryList)
                }
            },
            interactionSource = interactionSource,
            activeItemId = activeItemId,
            onClearActiveItem = { activeItemId = null },
            onTodoItemModified = { id: Long, content: String ->
                viewModel.modifyTodo(
                    item = ModifyTodoRequestModel(
                        todoId = id,
                        content = TodoContentModel(
                            content = content
                        )
                    )
                )
            },
            resetNewItemFlag = { viewModel.updateNewItemFlag(false) },
            onDragEnd = { viewModel.onDragEnd() },
            onCategoryDragEnd = { viewModel.onCategoryDragEnd() },
            onMove = { from, to -> viewModel.onMove(from, to) },
            onMoveCategory = { from, to -> viewModel.onMoveCategory(from, to) },
            isDropDownMenuExpanded = isDropDownMenuExpanded,
            onDropdownExpandedChange = { isDropDownMenuExpanded = it },
            haptic = haptic
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray100)
        )
    }
}

@Composable
fun BacklogContent(
    uiState: BacklogPageState = BacklogPageState(),
    onValueChange: (String) -> Unit = {},
    createBacklog: (String) -> Unit = {},
    onClickYesterdayList: () -> Unit = {},
    onSelectCategory: (Int) -> Unit = {},
    onClickCategoryAdd: () -> Unit = {},
    onClickCategoryDeleteDropdown: () -> Unit = {},
    onClickCategoryModifyDropdown: () -> Unit = {},
    onItemSwiped: (TodoItemModel) -> Unit = {},
    onClickBtnTodoSettings: (Int) -> Unit = {},
    interactionSource: MutableInteractionSource,
    activeItemId: Long?,
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = { _, _ -> },
    resetNewItemFlag: () -> Unit = {},
    onDragEnd: (List<TodoItemModel>) -> Unit = {},
    onCategoryDragEnd: () -> Unit = {},
    onMove: (Int, Int) -> Unit,
    onMoveCategory: (Int, Int) -> Unit,
    isDropDownMenuExpanded: Boolean = false,
    onDropdownExpandedChange: (Boolean) -> Unit = {},
    haptic: HapticFeedback = LocalHapticFeedback.current
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {
        BacklogCategoryList(
            onClickCategoryAdd = onClickCategoryAdd,
            categoryList = uiState.categoryList,
            interactionSource = interactionSource,
            onSelectCategory = onSelectCategory,
            onMoveCategory = onMoveCategory,
            onCategoryDragEnd = onCategoryDragEnd,
            selectedCategoryIndex = uiState.selectedCategoryIndex
        )

        Box {
            TopBar(
                titleText = if (uiState.categoryList.isNotEmpty()) {
                    uiState.categoryList[uiState.selectedCategoryIndex].categoryName
                } else {
                    ALL
                },
                titleTextStyle = PoptatoTypo.lgSemiBold,
                subText = uiState.backlogList.size.toString(),
                subTextStyle = PoptatoTypo.lgMedium,
                subTextColor = Gray60,
                isCategorySettingBtn = (uiState.selectedCategoryIndex != 0 && uiState.selectedCategoryIndex != 1),
                isCategorySettingBtnSelected = { onDropdownExpandedChange(true) }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 31.dp, top = 42.dp)
            ) {
                DropdownMenu(
                    shape = RoundedCornerShape(12.dp),
                    containerColor = Gray95,
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { onDropdownExpandedChange(false) }
                ) {
                    CategoryDropDownItem(
                        itemIcon = R.drawable.ic_pen,
                        itemText = modify,
                        textColor = Gray30,
                        onClickItemDropdownItem = onClickCategoryModifyDropdown
                    )

                    Divider(color = Gray90)

                    CategoryDropDownItem(
                        itemIcon = R.drawable.ic_trash,
                        itemText = DELETE_ACTION,
                        textColor = Danger50,
                        onClickItemDropdownItem = onClickCategoryDeleteDropdown
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                CreateBacklogTextFiled(
                    onValueChange = onValueChange,
                    taskInput = uiState.taskInput,
                    createBacklog = createBacklog
                )

                if (uiState.backlogList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = EmptyBacklogTitle,
                            style = PoptatoTypo.lgMedium,
                            textAlign = TextAlign.Center,
                            color = Gray80
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))

                    BacklogTaskList(
                        backlogList = uiState.backlogList,
                        onItemSwiped = onItemSwiped,
                        onClickBtnTodoSettings = onClickBtnTodoSettings,
                        activeItemId = activeItemId,
                        onClearActiveItem = onClearActiveItem,
                        onTodoItemModified = onTodoItemModified,
                        isNewItemCreated = uiState.isNewItemCreated,
                        resetNewItemFlag = resetNewItemFlag,
                        onDragEnd = onDragEnd,
                        onMove = onMove,
                        haptic = haptic
                    )
                }
            }

            if (!uiState.isYesterdayListEmpty) {
                BacklogGuideItem(
                    onClickYesterdayList = onClickYesterdayList,
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    interactionSource = interactionSource
                )
            }
        }
    }
}

@Composable
fun CategoryDropDownItem(
    itemIcon: Int,
    itemText: String,
    textColor: Color,
    onClickItemDropdownItem: () -> Unit = {}
) {
    DropdownMenuItem(
        contentPadding = PaddingValues(0.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = itemIcon),
                contentDescription = "category icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(16.dp)
            )
        },
        text = {
            Text(
                text = itemText,
                color = textColor,
                style = PoptatoTypo.smMedium,
                modifier = Modifier
            )
        },
        onClick = { onClickItemDropdownItem() }
    )
}

@Composable
fun BacklogCategoryList(
    interactionSource: MutableInteractionSource,
    categoryList: List<CategoryItemModel> = emptyList(),
    onClickCategoryAdd: () -> Unit = {},
    onSelectCategory: (Int) -> Unit = {},
    onMoveCategory: (Int, Int) -> Unit,
    onCategoryDragEnd: () -> Unit = {},
    selectedCategoryIndex: Int = 0
) {
    var draggedItem by remember { mutableStateOf<CategoryItemModel?>(null) }
    var isDragging by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val dragDropState = rememberDragDropListState(
        lazyListState = rememberLazyListState(),
        onMove = onMoveCategory
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp)
    ) {
        LazyRow(
            state = dragDropState.lazyListState,
            modifier = Modifier
                .weight(1f)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            dragDropState.onHorizontalDragStart(offset)
                            if (dragDropState.currentIndexOfDraggedItem == 0 || dragDropState.currentIndexOfDraggedItem == 1) return@detectDragGesturesAfterLongPress
                            draggedItem = categoryList[dragDropState.currentIndexOfDraggedItem
                                ?: return@detectDragGesturesAfterLongPress]
                            isDragging = true
                        },
                        onDragEnd = {
                            dragDropState.onDragInterrupted()
                            draggedItem = null
                            isDragging = false
                            onCategoryDragEnd()
                        },
                        onDragCancel = {
                            dragDropState.onDragInterrupted()
                            draggedItem = null
                            isDragging = false
                        },
                        onDrag = { change, offset ->
                            change.consume()

                            val currentIndex = dragDropState.currentIndexOfDraggedItem
                            if (currentIndex != null && currentIndex > 1) {
                                dragDropState.onHorizontalDrag(offset)
                            }

                            if (dragDropState.overscrollJob?.isActive == true) return@detectDragGesturesAfterLongPress
                            dragDropState
                                .checkForOverScrollHorizontal()
                                .takeIf { it != 0f }
                                ?.let {
                                    dragDropState.overscrollJob = scope.launch {
                                        val adjustedScroll = it * 0.3f
                                        dragDropState.lazyListState.scrollBy(adjustedScroll)
                                    }
                                } ?: run { dragDropState.overscrollJob?.cancel() }
                        }
                    )
                },
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val categoryFixedIcon: List<Int> =
                listOf(R.drawable.ic_category_all, R.drawable.ic_category_star)

            itemsIndexed(categoryList, key = { _, item -> item.categoryId }) { index, item ->
                val isDragged = (index == dragDropState.currentIndexOfDraggedItem && index != 0 && index != 1)

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationX =
                                dragDropState.horizontalElementDisplacement.takeIf { isDragged }
                                    ?: 0f
                            scaleX = if (isDragged) 1.05f else 1f
                            scaleY = if (isDragged) 1.05f else 1f
                        }
                        .border(
                            if (isDragged) BorderStroke(1.dp, Color.White) else BorderStroke(
                                0.dp,
                                Color.Transparent
                            ),
                            CircleShape
                        )
                ) {
                    CategoryListIcon(
                        imgResource = if (index == 0 || index == 1) categoryFixedIcon[index] else -1,
                        paddingStart = if (index == 0) 16 else 0,
                        imgUrl = item.categoryImgUrl,
                        isSelected = selectedCategoryIndex == index,
                        onClickCategory = { onSelectCategory(index) }
                    )
                }
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_add_category),
            contentDescription = "add backlog category",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(40.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickCategoryAdd() }
                )
        )
    }
}

@Composable
fun CategoryListIcon(
    paddingStart: Int = 0,
    paddingHorizontal: Int = 0,
    imgResource: Int = -1,
    imgUrl: String = "",
    isSelected: Boolean,
    interactionSource: MutableInteractionSource = remember{ MutableInteractionSource() },
    onClickCategory: () -> Unit = {},
) {

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    Coil.setImageLoader(imageLoader)

    Box(
        modifier = Modifier
            .padding(start = paddingStart.dp)
            .padding(horizontal = paddingHorizontal.dp)
            .size(40.dp)
            .border(width = 1.dp, color = if (isSelected) Gray00 else Gray95, shape = CircleShape)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { onClickCategory() }
            )
    ) {
        AsyncImage(
            model = if (imgResource == -1) imgUrl else imgResource,
            contentDescription = "category icon",
            modifier = Modifier
                .align(Alignment.Center)
                .size(24.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun BacklogTaskList(
    backlogList: List<TodoItemModel> = emptyList(),
    onItemSwiped: (TodoItemModel) -> Unit = {},
    onClickBtnTodoSettings: (Int) -> Unit = {},
    activeItemId: Long?,
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = { _, _ -> },
    isNewItemCreated: Boolean = false,
    resetNewItemFlag: () -> Unit = {},
    onDragEnd: (List<TodoItemModel>) -> Unit = { },
    onMove: (Int, Int) -> Unit,
    haptic: HapticFeedback = LocalHapticFeedback.current
) {
    var draggedItem by remember { mutableStateOf<TodoItemModel?>(null) }
    var isDragging by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val dragDropState = rememberDragDropListState(
        lazyListState = rememberLazyListState(),
        onMove = onMove
    )

    LazyColumn(
        state = dragDropState.lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        dragDropState.onDragStart(offset)
                        draggedItem = backlogList[dragDropState.currentIndexOfDraggedItem
                            ?: return@detectDragGesturesAfterLongPress]
                        AnalyticsManager.logEvent(
                            eventName = "drag_tasks",
                            params = mapOf("task_ID" to "${draggedItem?.todoId}")
                        )
                        isDragging = true
                    },
                    onDragEnd = {
                        dragDropState.onDragInterrupted()
                        draggedItem = null
                        isDragging = false
                        onDragEnd(backlogList)
                    },
                    onDragCancel = {
                        dragDropState.onDragInterrupted()
                        draggedItem = null
                        isDragging = false
                    },
                    onDrag = { change, offset ->
                        change.consume()
                        dragDropState.onDrag(offset)
                        if (dragDropState.overscrollJob?.isActive == true) return@detectDragGesturesAfterLongPress
                        dragDropState
                            .checkForOverScroll()
                            .takeIf { it != 0f }
                            ?.let {
                                dragDropState.overscrollJob = scope.launch {
                                    val adjustedScroll = it * 0.3f
                                    dragDropState.lazyListState.scrollBy(adjustedScroll)
                                }
                            } ?: run { dragDropState.overscrollJob?.cancel() }
                    }
                )
            }
    ) {
        itemsIndexed(backlogList, key = { _, item -> item.todoId }) { index, item ->
            var offsetX by remember { mutableFloatStateOf(0f) }
            val isDragged = index == dragDropState.currentIndexOfDraggedItem
            val isActive = activeItemId == item.todoId

            Box(
                modifier = Modifier
                    .zIndex(if (index == dragDropState.currentIndexOfDraggedItem) 1f else 0f)
                    .graphicsLayer {
                        translationY =
                            dragDropState.elementDisplacement.takeIf { index == dragDropState.currentIndexOfDraggedItem }
                                ?: 0f
                        scaleX = if (isDragged) 1.05f else 1f
                        scaleY = if (isDragged) 1.05f else 1f
                    }
                    .offset { IntOffset(offsetX.toInt(), 0) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX < -300f) {
                                    onItemSwiped(item)
                                    offsetX = 0f
                                } else {
                                    offsetX = 0f
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                if (offsetX + dragAmount <= 0f) {
                                    offsetX += dragAmount
                                }
                            }
                        )
                    }
                    .then(
                        if (!isDragging) {
                            Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                        } else {
                            Modifier
                        }
                    )
                    .border(
                        if (isDragged) BorderStroke(1.dp, Color.White) else BorderStroke(
                            0.dp,
                            Color.Transparent
                        ),
                        RoundedCornerShape(8.dp)
                    )
            ) {
                BacklogItem(
                    item = item,
                    index = index,
                    isActive = isActive,
                    onClickBtnTodoSettings = onClickBtnTodoSettings,
                    onClearActiveItem = onClearActiveItem,
                    onTodoItemModified = onTodoItemModified
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        item { Spacer(modifier = Modifier.height(45.dp)) }
    }

    LaunchedEffect(isNewItemCreated) {
        if (isNewItemCreated && !isDragging) {
            dragDropState.lazyListState.scrollToItem(0)
            resetNewItemFlag()
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun BacklogItem(
    item: TodoItemModel,
    index: Int = -1,
    isActive: Boolean = false,
    modifier: Modifier = Modifier,
    onClickBtnTodoSettings: (Int) -> Unit = {},
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = { _, _ -> }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gray95)
            .padding(bottom = 16.dp)
            .padding(start = 16.dp, end = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current
        var textFieldValue by remember {
            mutableStateOf(
                TextFieldValue(
                    text = item.content,
                    selection = TextRange(item.content.length)
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .padding(top = if (item.isBookmark || item.dDay != null || item.isRepeat) 12.dp else 0.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                if (item.isBookmark) {
                    BookmarkItem()
                    Spacer(modifier = Modifier.width(6.dp))
                }
                if (item.isRepeat) {
                    RepeatItem()
                    Spacer(modifier = Modifier.width(6.dp))
                }
                if (item.dDay != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = Gray90, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = formatDeadline(item.dDay),
                            style = PoptatoTypo.xsSemiBold,
                            color = Gray70
                        )
                    }
                }
            }

            if (!item.isBookmark && item.dDay == null && !item.isRepeat) Spacer(modifier = Modifier.height(16.dp)) else Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (isActive) {

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newTextFieldValue ->
                        textFieldValue = newTextFieldValue
                    },
                    textStyle = PoptatoTypo.mdRegular.copy(color = Gray00),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                textFieldValue = textFieldValue.copy(
                                    selection = TextRange(textFieldValue.text.length)
                                )
                            }
                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onClearActiveItem()
                            if (item.content != textFieldValue.text) onTodoItemModified(
                                item.todoId,
                                textFieldValue.text
                            )
                        }
                    ),
                    cursorBrush = SolidColor(Gray00)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.content,
                        style = PoptatoTypo.mdRegular,
                        color = Gray00
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_three_dot),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clickable {
                        onClickBtnTodoSettings(index)
                    }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateBacklogTextFiled(
    taskInput: String = "",
    onValueChange: (String) -> Unit = {},
    createBacklog: (String) -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val imeVisible = WindowInsets.isImeVisible

    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus()
        }
    }


    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isFocused) Gray00 else Gray70,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        BasicTextField(
            value = taskInput,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(start = 16.dp, end = 28.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            textStyle = PoptatoTypo.mdRegular,
            cursorBrush = SolidColor(Gray00),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (taskInput.isNotEmpty()) {
                        createBacklog(taskInput)
                        onValueChange("")
                    } else {
                        focusManager.clearFocus()
                    }
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (taskInput.isEmpty() && !isFocused) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = BacklogHint,
                            style = PoptatoTypo.mdMedium,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (taskInput.isNotEmpty()) {
            IconButton(
                onClick = { onValueChange("") },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun BacklogGuideItem(
    onClickYesterdayList: () -> Unit = {},
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(Primary60)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = BACKLOG_YESTERDAY_TASK_GUIDE,
            style = PoptatoTypo.smMedium,
            color = Gray100,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickYesterdayList() }
                )

        ) {
            Text(
                text = CONFIRM_ACTION,
                style = PoptatoTypo.smSemiBold,
                color = Gray100
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }
    }
}

