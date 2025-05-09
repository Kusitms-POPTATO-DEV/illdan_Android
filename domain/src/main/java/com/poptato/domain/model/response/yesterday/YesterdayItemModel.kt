package com.poptato.domain.model.response.yesterday

import com.poptato.domain.model.enums.TodoStatus

data class YesterdayItemModel (
    val todoId: Long = -1,
    val content: String = "",
    val todoStatus: TodoStatus = TodoStatus.INCOMPLETE,
    val dday: Int? = null,
    val bookmark: Boolean = false,
    val repeat: Boolean = false
)