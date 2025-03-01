package com.poptato.data.model.response.history

import com.google.gson.annotations.SerializedName

class HistoryCalendarListResponse (
    @SerializedName("dates")
    val dates: List<String> = emptyList(),
)