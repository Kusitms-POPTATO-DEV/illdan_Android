package com.poptato.domain.model.request.todo

data class UpdateDeadlineRequestModel(
    val todoId: Long,
    val deadline: DeadlineContentModel
)
