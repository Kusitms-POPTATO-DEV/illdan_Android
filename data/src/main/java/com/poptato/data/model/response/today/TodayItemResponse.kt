package com.poptato.data.model.response.today

import com.google.gson.annotations.SerializedName
import com.poptato.domain.model.enums.TodoStatus

data class TodayItemResponse(
    @SerializedName("todoId")
    val todoId: Long = -1,
    @SerializedName("content")
    val content: String = "",
    @SerializedName("todayStatus")
    val todoStatus: String = "",
    @SerializedName("isBookmark")
    val isBookmark: Boolean = false,
    @SerializedName("isRepeat")
    val isRepeat: Boolean = false,
    @SerializedName("deadline")
    val deadline: String? = null,
    @SerializedName("dDay")
    val dDay: Int? = null
)