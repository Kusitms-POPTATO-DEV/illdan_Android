package com.poptato.domain.model.response.today

data class TodayListModel(
    val date: String = "",
    val todays: List<TodoItemModel> = emptyList(),
    val totalPageCount: Int = -1
)
