package com.poptato.ui.util

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DragDropListState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit,
    val scope: CoroutineScope
) {
    var draggedDistance by mutableFloatStateOf(0f)
    var horizontalDraggedDistance by mutableFloatStateOf(0f)
    private var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)
    private val initialOffsets: Pair<Int, Int>?
        get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }
    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)
    val elementDisplacement
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absolute = it)
        }?.let { item ->
            (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset
        }
    val horizontalElementDisplacement
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absolute = it)
        }?.let { item ->
            (initiallyDraggedElement?.offset ?: 0f).toFloat() + horizontalDraggedDistance - item.offset
        }
    private val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absolute = it)
        }
    var overscrollJob by mutableStateOf<Job?>(null)

    fun onDragStart(offset: Offset) {
        scope.launch { lazyListState.scrollToItem(lazyListState.firstVisibleItemIndex) }

        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            offset.y.toInt() in item.offset..(item.offset + item.size)
        }?.also {
            currentIndexOfDraggedItem = it.index
            initiallyDraggedElement = it
        }
    }

    fun onHorizontalDragStart(offset: Offset) {
        scope.launch { lazyListState.scrollToItem(lazyListState.firstVisibleItemIndex) }

        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            offset.x.toInt() in item.offset..(item.offset + item.size)
        }?.also {
            currentIndexOfDraggedItem = it.index
            initiallyDraggedElement = it
        }
    }

    fun onDragInterrupted() {
        draggedDistance = 0f
        horizontalDraggedDistance = 0f
        currentIndexOfDraggedItem = null
        initiallyDraggedElement = null
        overscrollJob?.cancel()
    }

    fun onDrag(offset: Offset) {
        draggedDistance += offset.y

        initialOffsets?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo.filterNot { item ->
                    item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index
                }.firstOrNull { item ->
                    val delta = startOffset - hovered.offset
                    when {
                        delta > 0 -> (endOffset > item.offsetEnd)
                        else -> (startOffset < item.offset)
                    }
                }?.also { item ->
                    currentIndexOfDraggedItem?.let { current ->
                        onMove.invoke(current, item.index)
                    }
                    currentIndexOfDraggedItem = item.index
                }
            }
        }
    }

    fun onHorizontalDrag(offset: Offset) {
        horizontalDraggedDistance += offset.x

        initialOffsets?.let { (startOffset, endOffset) ->
            val startOffsetX = startOffset + horizontalDraggedDistance
            val endOffsetX = endOffset + horizontalDraggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo.filterNot { item ->
                    item.offsetEnd < startOffsetX || item.offset > endOffsetX || hovered.index == item.index
                }.firstOrNull { item ->
                    val delta = startOffsetX - hovered.offset
                    when {
                        delta > 0 -> (endOffsetX > item.offsetEnd)
                        else -> (startOffsetX < item.offset)
                    }
                }?.also { item ->
                    currentIndexOfDraggedItem?.let { current ->
                        onMove.invoke(current, item.index)
                    }
                    currentIndexOfDraggedItem = item.index
                }
            }
        }
    }

    fun checkForOverScroll(): Float {
        return initiallyDraggedElement?.let {
            val startOffset = it.offset + draggedDistance
            val endOffset = it.offsetEnd + draggedDistance

            return@let when {
                draggedDistance > 0 -> (endOffset - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
                draggedDistance < 0 -> (startOffset - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
                else -> null
            }
        } ?: 0f
    }

    fun checkForOverScrollHorizontal(): Float {
        return initiallyDraggedElement?.let {
            val startOffsetX = it.offset + draggedDistance
            val endOffsetX = it.offsetEnd + draggedDistance

            return@let when {
                draggedDistance > 0 -> (endOffsetX - lazyListState.layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 }
                draggedDistance < 0 -> (startOffsetX - lazyListState.layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 }
                else -> null
            }
        } ?: 0f
    }
}

@Composable
fun rememberDragDropListState(
    lazyListState: LazyListState = rememberLazyListState(),
    onMove: (Int, Int) -> Unit,
): DragDropListState {
    val scope = rememberCoroutineScope()
    return remember {
        DragDropListState(
            lazyListState = lazyListState,
            onMove = onMove,
            scope = scope
        )
    }
}