package com.poptato.yesterdaylist

import com.poptato.domain.model.response.yesterday.YesterdayItemModel
import com.poptato.ui.base.PageState

data class YesterdayListPageState(
    val yesterdayList: List<YesterdayItemModel> = emptyList(),
    val completedTodoList: List<Long> = emptyList(),
    val totalPageCount: Int = -1
) : PageState