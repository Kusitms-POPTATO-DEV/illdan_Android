package com.poptato.today

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.core.util.TimeFormatter
import com.poptato.design_system.BtnGetTodoText
import com.poptato.design_system.SNACK_BAR_COMPLETE_DELETE_TODO
import com.poptato.design_system.ERROR_GENERIC_MESSAGE
import com.poptato.design_system.EmptyTodoTitle
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray90
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary100
import com.poptato.design_system.Primary60
import com.poptato.design_system.R
import com.poptato.design_system.SNACK_BAR_TODAY_ALL_CHECKED
import com.poptato.design_system.TodayTopBarSub
import com.poptato.domain.model.enums.TodoStatus
import com.poptato.domain.model.request.todo.ModifyTodoRequestModel
import com.poptato.domain.model.request.todo.TodoContentModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.common.BookmarkItem
import com.poptato.ui.common.PoptatoCheckBox
import com.poptato.ui.common.RepeatItem
import com.poptato.ui.common.TopBar
import com.poptato.ui.common.formatDeadline
import com.poptato.ui.util.AnalyticsManager
import com.poptato.ui.util.LoadingManager
import com.poptato.ui.util.rememberDragDropListState
import com.poptato.ui.util.toPx
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun TodayScreen(
    goToBacklog: () -> Unit = {},
    showSnackBar: (String) -> Unit,
    showBottomSheet: (TodoItemModel, List<CategoryItemModel>) -> Unit = { _, _ -> },
    updateDeadlineFlow: SharedFlow<String?>,
    deleteTodoFlow: SharedFlow<Long>,
    activateItemFlow: SharedFlow<Long>,
    updateTodoRepeatFlow: SharedFlow<Long>,
    updateBookmarkFlow: SharedFlow<Long>,
    updateCategoryFlow: SharedFlow<Long?>,
) {
    val viewModel: TodayViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val date = TimeFormatter.getTodayMonthDay()
    var activeItemId by remember { mutableStateOf<Long?>(null) }
    val haptic = LocalHapticFeedback.current

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

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is TodayEvent.OnFailedUpdateTodayList -> {
                    showSnackBar(ERROR_GENERIC_MESSAGE)
                }
                is TodayEvent.OnSuccessDeleteTodo -> {
                    showSnackBar(SNACK_BAR_COMPLETE_DELETE_TODO)
                }
                is TodayEvent.TodayAllChecked -> {
                    scope.launch {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        delay(300)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showSnackBar(SNACK_BAR_TODAY_ALL_CHECKED)
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.isFinishedInitialization) {
        if (uiState.isFinishedInitialization) {
            LoadingManager.endLoading()
        }
    }

    if (uiState.isFinishedInitialization) {
        TodayContent(
            date = date,
            uiState = uiState,
            onCheckedChange = { status, id ->
                viewModel.onCheckedTodo(status = status, id = id)
            },
            onClickBtnGetTodo = { goToBacklog() },
            onItemSwiped = { itemToRemove -> viewModel.swipeTodayItem(itemToRemove) },
            onMove = { from, to -> viewModel.moveItem(from, to) },
            onDragEnd = { viewModel.onDragEnd() },
            showBottomSheet = {
                viewModel.getSelectedItemDetailContent(it) { callback ->
                    showBottomSheet(callback, uiState.categoryList)

                    AnalyticsManager.logEvent(
                        eventName = "today_bottom_sheet"
                    )
                }
            },
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
fun TodayContent(
    date: String = "",
    uiState: TodayPageState = TodayPageState(),
    onCheckedChange: (TodoStatus, Long) -> Unit = {_, _ ->},
    onClickBtnGetTodo: () -> Unit = {},
    onItemSwiped: (TodoItemModel) -> Unit = {},
    onMove: (fromIndex: Int, toIndex: Int) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit,
    showBottomSheet: (TodoItemModel) -> Unit = {},
    activeItemId: Long?,
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = {_,_ ->},
    haptic: HapticFeedback = LocalHapticFeedback.current
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {
        TopBar(
            titleText = date,
            titleTextStyle = PoptatoTypo.xxxLSemiBold,
            titleTextColor = Gray00,
            subText = TodayTopBarSub,
            subTextStyle = PoptatoTypo.mdMedium,
            subTextColor = Gray40,
            isTodayTopBar = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.todayList.isEmpty()) EmptyTodoView(
                onClickBtnGetTodo = onClickBtnGetTodo
            )
            else TodayTodoList(
                list = uiState.todayList,
                onCheckedChange = onCheckedChange,
                onItemSwiped = onItemSwiped,
                onMove = onMove,
                onDragEnd = onDragEnd,
                showBottomSheet = showBottomSheet,
                activeItemId = activeItemId,
                onClearActiveItem = onClearActiveItem,
                onTodoItemModified = onTodoItemModified,
                haptic = haptic
            )
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TodayTodoList(
    list: List<TodoItemModel> = emptyList(),
    onCheckedChange: (TodoStatus, Long) -> Unit = { _, _ -> },
    onItemSwiped: (TodoItemModel) -> Unit = {},
    onMove: (fromIndex: Int, toIndex: Int) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit,
    showBottomSheet: (TodoItemModel) -> Unit = {},
    activeItemId: Long?,
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = {_,_ ->},
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
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        dragDropState.onDragStart(offset)
                        draggedItem = list[dragDropState.currentIndexOfDraggedItem
                            ?: return@detectDragGesturesAfterLongPress]
                        isDragging = true
                    },
                    onDragEnd = {
                        dragDropState.onDragInterrupted()
                        draggedItem = null
                        isDragging = false
                        onDragEnd()
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
                                    val adjustedScroll = it * 0.5f
                                    dragDropState.lazyListState.scrollBy(adjustedScroll)
                                }
                            } ?: run { dragDropState.overscrollJob?.cancel() }
                    }
                )
            }
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        itemsIndexed(items = list, key = { index, item -> item.todoId }) { index, item ->
            var offsetX by remember { mutableFloatStateOf(0f) }
            val isDragged = index == dragDropState.currentIndexOfDraggedItem
            val isActive = activeItemId == item.todoId

            TodayTodoItem(
                item = item,
                onCheckedChange = onCheckedChange,
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
                    .pointerInput(item.todoStatus) {
                        if (item.todoStatus == TodoStatus.COMPLETED) return@pointerInput
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX > 200f) {
                                    onItemSwiped(item)
                                } else {
                                    offsetX = 0f
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                if (offsetX + dragAmount >= 0f) {
                                    offsetX += dragAmount
                                }
                            }
                        )
                    }
                    .then(
                        if (!isDragging) {
                            Modifier.animateItem(
                                fadeInSpec = null,
                                fadeOutSpec = null
                            )
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
                    ),
                showBottomSheet = showBottomSheet,
                isActive = isActive,
                onClearActiveItem = onClearActiveItem,
                onTodoItemModified = onTodoItemModified
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun TodayTodoItem(
    item: TodoItemModel = TodoItemModel(),
    onCheckedChange: (TodoStatus, Long) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    showBottomSheet: (TodoItemModel) -> Unit = {},
    isActive: Boolean,
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = {_,_ ->}
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
    var showAnimation by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = showAnimation, label = "")

    LaunchedEffect(transition) {
        snapshotFlow { transition.currentState }
            .filter { it }
            .collect {
                showAnimation = false
            }
    }

    val elevation by transition.animateDp(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) 8.dp else 0.dp
    }

    val scale by transition.animateFloat(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) 1.1f else 1f
    }

    val yOffset by transition.animateFloat(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) -50f else 0f
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Gray95)
            .offset { IntOffset(0, yOffset.toInt()) }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                shadowElevation = elevation.toPx()
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp,
                        top = if (item.isBookmark || item.dDay != null || item.isRepeat) 8.dp else 16.dp
                    )
                    .padding(start = 16.dp, end = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PoptatoCheckBox(
                    isChecked = item.todoStatus == TodoStatus.COMPLETED,
                    onCheckedChange = {
                        onCheckedChange(item.todoStatus, item.todoId)
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

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
                                if (item.content != textFieldValue.text) onTodoItemModified(item.todoId, textFieldValue.text)
                            }
                        ),
                        cursorBrush = SolidColor(Gray00)
                    )
                } else {
                    Text(
                        text = item.content,
                        color = Gray00,
                        style = PoptatoTypo.mdRegular,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_three_dot),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.clickable { showBottomSheet(item) }
        )

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun EmptyTodoView(
    onClickBtnGetTodo: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = EmptyTodoTitle,
            color = Gray40,
            style = PoptatoTypo.lgMedium
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .size(width = 132.dp, height = 37.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Primary60)
                .clickable { onClickBtnGetTodo() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_list_selected),
                contentDescription = "",
                tint = Primary100,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = BtnGetTodoText,
                style = PoptatoTypo.smSemiBold,
                color = Primary100
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewToday() {
//    TodayContent()
}