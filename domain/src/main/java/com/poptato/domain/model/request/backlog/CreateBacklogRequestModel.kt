package com.poptato.domain.model.request.backlog

data class CreateBacklogRequestModel(
    val categoryId: Long = -1,
    val content: String = ""
)
