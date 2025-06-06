package com.poptato.data.model.response.yesterday

import com.google.gson.annotations.SerializedName

data class YesterdayListResponse(
    @SerializedName("yesterdays")
    val yesterdays: List<YesterdayListItemResponse> = emptyList(),
    @SerializedName("totalPageCount")
    val totalPageCount: Int = -1,
)