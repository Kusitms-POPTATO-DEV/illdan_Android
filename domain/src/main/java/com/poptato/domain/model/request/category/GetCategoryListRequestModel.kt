package com.poptato.domain.model.request.category

data class GetCategoryListRequestModel (
    val page: Int = 0,
    val size: Int = 8
)