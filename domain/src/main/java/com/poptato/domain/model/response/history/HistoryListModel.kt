package com.poptato.domain.model.response.history

data class HistoryListModel(
    val histories: List<HistoryItemModel> = emptyList(),
    val totalPageCount: Int = -1
)