package com.poptato.domain.model.response.yesterday

data class YesterdayListModel (
    val yesterdays: List<YesterdayItemModel> = emptyList(),
    val totalPageCount: Int = -1
)