package com.poptato.domain.model.response.category

data class CategoryIconTypeListModel (
    val iconType: String = "",
    val icons: List<CategoryIconItemModel> = emptyList()
)