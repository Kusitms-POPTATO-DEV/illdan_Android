package com.poptato.ui.common

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poptato.design_system.Confirm
import com.poptato.design_system.DELETE
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray50
import com.poptato.design_system.Gray60
import com.poptato.design_system.Gray90
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary40
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.util.fadingEdge
import timber.log.Timber

@Composable
fun TimePickerBottomSheet(
    item: TodoItemModel,
    onDismissRequest: () -> Unit = {},
    onClickCompletionButton: (Pair<Long, Triple<String, Int, Int>?>) -> Unit = {}
) {
    val period = listOf("오전", "오후")
    val hours = (1..12).toList()
    val minutes = (0..55 step 5).toList()
    val periodState = rememberLazyListState(
        initialFirstVisibleItemIndex = when (item.meridiem) {
            "오전" -> 0
            "오후" -> 1
            else -> 0
        }
    )
    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = item.hour)
    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = item.minute.div(5))

    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 20.dp)
    ) {
        TimePickerSelector(
            periodState = periodState,
            hourState = hourState,
            minuteState = minuteState
        )

        Spacer(modifier = Modifier.height(32.dp))

        BottomSheetActionButtons(
            onDelete = {
                onClickCompletionButton(Pair(item.todoId, null))
                onDismissRequest()
            },
            onConfirm = {
                val selected = Triple(
                    period[periodState.firstVisibleItemIndex],
                    hours[hourState.firstVisibleItemIndex],
                    minutes[minuteState.firstVisibleItemIndex]
                )
                onClickCompletionButton(Pair(item.todoId, selected))
                Timber.e(selected.toString())
                onDismissRequest()
            }
        )
    }
}

@Composable
fun DialPicker(
    items: List<String>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val extendedItems = listOf("") + items + listOf("")
    val visibleItemsCount = 3
    val itemHeight = 33.dp
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .height(itemHeight * visibleItemsCount)
            .fadingEdge(fadingEdgeGradient),
        flingBehavior = flingBehavior,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(extendedItems.size) { index ->
            val item = extendedItems[index]
            val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
            val fontStyle = when (index) {
                firstVisibleItemIndex + 1 -> PoptatoTypo.xLSemiBold
                else -> PoptatoTypo.lgMedium
            }
            val color = when(index) {
                firstVisibleItemIndex + 1 -> Gray00
                else -> Gray60
            }

            Box(
                modifier = Modifier
                    .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                if (item.isNotEmpty()) {
                    Text(
                        text = item,
                        style = fontStyle,
                        textAlign = TextAlign.Center,
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
fun TimePickerSelector(
    periodState: LazyListState,
    hourState: LazyListState,
    minuteState: LazyListState,
    modifier: Modifier = Modifier
) {
    val period = listOf("오전", "오후")
    val hour = (1..12).toList().map { it.toString().padStart(2, '0') }
    val minute = (0..55 step 5).toList().map { it.toString().padStart(2, '0') }

    Row(
        modifier = modifier.padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DialPicker(items = period, listState = periodState, modifier = Modifier.weight(1f))
        DialPicker(items = hour, listState = hourState, modifier = Modifier.weight(1f))
        DialPicker(items = minute, listState = minuteState, modifier = Modifier.weight(1f))
    }
}


@Composable
fun BottomSheetActionButtons(
    onDelete: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        PoptatoButton(
            buttonText = DELETE,
            textColor = Gray50,
            backgroundColor = Gray95,
            modifier = Modifier.weight(1f),
            onClickButton = onDelete
        )

        Spacer(modifier = Modifier.width(12.dp))

        PoptatoButton(
            buttonText = Confirm,
            textColor = Gray90,
            backgroundColor = Primary40,
            modifier = Modifier.weight(1f),
            onClickButton = onConfirm
        )
    }
}
