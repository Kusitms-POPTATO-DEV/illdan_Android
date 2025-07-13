package com.poptato.backlog

import com.poptato.design_system.ALL
import com.poptato.design_system.BOOKMARK
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.base.PageState

data class BacklogPageState(
    val backlogList: List<TodoItemModel> = emptyList(),
    val showTodoBottomSheet: Boolean = false,
    val selectedItem: TodoItemModel = TodoItemModel(),
    val totalItemCount: Int = -1,
    val totalPageCount: Int = -1,
    val isNewItemCreated: Boolean = false,
    val currentPage: Int = 0,
    val isFinishedInitialization: Boolean = false,
    val categoryList: List<CategoryItemModel> = listOf(
        CategoryItemModel(categoryId = -1, categoryName = ALL),
        CategoryItemModel(categoryId = 0, categoryName = BOOKMARK)
    ),
    val selectedCategoryIndex: Int = 0,
    val selectedCategoryId: Long = -1,
    val isDeadlineDateMode: Boolean = false,
    val activeItemId: Long? = null
): PageState