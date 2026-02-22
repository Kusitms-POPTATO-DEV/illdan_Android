package com.poptato.data.model.response.category

import com.google.gson.annotations.SerializedName

data class CategoryListResponse (
    @SerializedName("categories")
    val categories: List<CategoryListItemResponse> = emptyList(),
    @SerializedName("totalPageCount")
    val totalPageCount: Int = -1
)