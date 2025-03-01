package com.poptato.domain.model.request.category

data class CategoryDragDropRequestModel(
    val categoryIds: List<Long> = emptyList()
)
