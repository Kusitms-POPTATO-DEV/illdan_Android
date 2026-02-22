package com.poptato.domain.model.request.category

data class ModifyCategoryRequestModel (
    val categoryId: Long = -1,
    val categoryModel: CategoryRequestModel = CategoryRequestModel()
)