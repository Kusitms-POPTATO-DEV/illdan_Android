package com.poptato.domain.model.response.today

import com.poptato.domain.model.enums.TodoStatus

data class TodoItemModel(
    var categoryId: Long = -1,
    val todoId: Long = -1,
    val content: String = "",
    val todoStatus: TodoStatus = TodoStatus.INCOMPLETE,
    val isBookmark: Boolean = false,
    val deadline: String = "",
    val isRepeat: Boolean = false,
    val dDay: Int? = null
)
