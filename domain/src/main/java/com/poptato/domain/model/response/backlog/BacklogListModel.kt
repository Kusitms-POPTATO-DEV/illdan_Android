package com.poptato.domain.model.response.backlog

import com.poptato.domain.model.response.today.TodoItemModel

data class BacklogListModel(
    val totalCount: Int = -1,
    val backlogs: List<TodoItemModel> = emptyList(),
    val totalPageCount: Int = -1,
    val categoryName: String = ""
)
