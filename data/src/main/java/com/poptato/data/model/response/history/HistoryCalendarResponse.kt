package com.poptato.data.model.response.history

import com.google.gson.annotations.SerializedName

data class HistoryCalendarResponse(
    @SerializedName("date")
    var date: String = "",
    @SerializedName("count")
    var count: Int = -1
)
