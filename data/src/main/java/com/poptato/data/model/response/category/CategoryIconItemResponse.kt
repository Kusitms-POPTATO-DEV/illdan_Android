package com.poptato.data.model.response.category

import com.google.gson.annotations.SerializedName

data class CategoryIconItemResponse (
    @SerializedName("emojiId")
    val emojiId: Long = -1,
    @SerializedName("imageUrl")
    val imageUrl: String = ""
)