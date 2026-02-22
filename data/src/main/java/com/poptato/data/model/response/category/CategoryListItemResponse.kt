package com.poptato.data.model.response.category

import com.google.gson.annotations.SerializedName

data class CategoryListItemResponse (
    @SerializedName("id")
    val id: Long = -1,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("emojiId")
    val emojiId: Long = -1,
    @SerializedName("imageUrl")
    val imageUrl: String = ""
)