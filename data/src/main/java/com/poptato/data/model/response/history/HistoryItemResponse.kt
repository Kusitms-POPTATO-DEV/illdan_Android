package com.poptato.data.model.response.history

import com.google.gson.annotations.SerializedName

data class HistoryItemResponse(
    @SerializedName("todoId")
    val todoId: Long = -1,
    @SerializedName("content")
    val content: String = "",
    @SerializedName("isCompleted")
    val isCompleted: Boolean = false
)