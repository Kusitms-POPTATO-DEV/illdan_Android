package com.poptato.domain.model.response.history

data class HistoryItemModel(
    val todoId: Long = -1,
    val content: String = "",
    val date: String = ""
)