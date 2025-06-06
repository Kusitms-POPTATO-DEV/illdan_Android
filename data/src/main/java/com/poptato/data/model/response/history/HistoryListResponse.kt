package com.poptato.data.model.response.history

import com.google.gson.annotations.SerializedName

data class HistoryListResponse(
    @SerializedName("histories")
    val histories: List<HistoryItemResponse> = emptyList(),
    @SerializedName("totalPageCount")
    val totalPageCount: Int = -1
)