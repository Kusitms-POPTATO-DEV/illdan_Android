package com.poptato.data.model.response.today

import com.google.gson.annotations.SerializedName

data class TodayListResponse(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("todays")
    val todays: List<TodayItemResponse> = emptyList(),
    @SerializedName("totalPageCount")
    val totalPageCount: Int = -1
)
