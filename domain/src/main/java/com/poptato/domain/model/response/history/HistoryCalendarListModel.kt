package com.poptato.domain.model.response.history

data class HistoryCalendarListModel (
    val dates: List<HistoryCalendarModel> = emptyList(),
)