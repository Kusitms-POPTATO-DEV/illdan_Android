package com.poptato.data.model.response.category

import com.google.gson.annotations.SerializedName

data class CategoryIconTotalListResponse (
    @SerializedName("groupEmojis")
    val groupEmojis: Map<String, List<CategoryIconItemResponse>> = emptyMap(),
    @SerializedName("totalPageCount")
    val totalPageCount: Int = -1
)