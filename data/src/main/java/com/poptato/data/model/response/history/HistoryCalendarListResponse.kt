package com.poptato.data.model.response.history

import com.google.gson.annotations.SerializedName

class HistoryCalendarListResponse (
    @SerializedName("historyCalendarList")
    val dates: List<HistoryCalendarResponse> = emptyList(),
)