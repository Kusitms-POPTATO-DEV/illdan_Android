package com.poptato.backlog

import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.base.PageState

data class BacklogPageState(
    val backlogList: List<TodoItemModel> = emptyList(),
    val taskInput: String = "",
    val showTodoBottomSheet: Boolean = false,
    val selectedItem: TodoItemModel = TodoItemModel(),
    val totalItemCount: Int = -1,
    val totalPageCount: Int = -1,
    val isExistYesterdayTodo: Boolean = false,
    val isNewItemCreated: Boolean = false,
    val currentPage: Int = 0,
    val isFinishedInitialization: Boolean = false,
    val categoryList: List<CategoryItemModel> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val selectedCategoryId: Long = -1,
    val isDeadlineDateMode: Boolean = false
): PageState