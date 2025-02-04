package com.poptato.data.model.response.backlog

import com.google.gson.annotations.SerializedName

data class BacklogItemResponse(
    @SerializedName("todoId")
    val todoId: Long = -1,
    @SerializedName("content")
    val content: String = "",
    @SerializedName("isBookmark")
    val isBookmark: Boolean = false,
    @SerializedName("isRepeat")
    val isRepeat: Boolean = false,
    @SerializedName("deadline")
    val deadline: String? = null,
    @SerializedName("dDay")
    val dDay: Int? = null
)
