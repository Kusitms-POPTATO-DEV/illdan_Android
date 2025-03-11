package com.poptato.domain.model.response.category

import com.poptato.domain.model.enums.CategoryScreenType

data class CategoryScreenContentModel (
    val screenType: CategoryScreenType = CategoryScreenType.Add,
    val categoryItem: CategoryItemModel = CategoryItemModel()
)