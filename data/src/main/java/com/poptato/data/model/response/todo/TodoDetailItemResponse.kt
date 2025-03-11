package com.poptato.data.model.response.todo

import com.google.gson.annotations.SerializedName

data class TodoDetailItemResponse (
    @SerializedName("isBookmark")
    val isBookmark: Boolean = false,
    @SerializedName("isRepeat")
    val isRepeat: Boolean = false,
    @SerializedName("content")
    val content: String = "",
    @SerializedName("deadline")
    val deadline: String? = "",
    @SerializedName("categoryName")
    val categoryName: String? = null,
    @SerializedName("emojiImageUrl")
    val emojiImageUrl: String? = null
)