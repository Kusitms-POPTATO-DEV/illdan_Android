package com.poptato.domain.model.request.backlog

data class GetBacklogListRequestModel(
    val categoryId: Long,
    val page: Int,
    val size: Int
)
