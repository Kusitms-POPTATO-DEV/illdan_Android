package com.poptato.today

import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.base.PageState

data class TodayPageState(
    val todayList: List<TodoItemModel> = emptyList(),
    val totalPageCount: Int = 0,
    val selectedItem: TodoItemModel = TodoItemModel(),
    val isFinishedInitialization: Boolean = false,
    val categoryList: List<CategoryItemModel> = emptyList(),
) : PageState
