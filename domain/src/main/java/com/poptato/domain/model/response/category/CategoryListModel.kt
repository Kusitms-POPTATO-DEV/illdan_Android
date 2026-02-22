package com.poptato.domain.model.response.category

data class CategoryListModel(
    val categoryList: List<CategoryItemModel> = emptyList(),
    val totalPageCount: Int = -1
)