package com.poptato.domain.model.response.category

data class CategoryItemModel (
    val categoryId: Long = -1,
    val iconId: Long = -1,
    val categoryName: String = "",
    val categoryImgUrl: String = ""
)