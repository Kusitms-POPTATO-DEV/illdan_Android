package com.poptato.core.util

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.reachedLastItem(): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index == layoutInfo.totalItemsCount - 1
}