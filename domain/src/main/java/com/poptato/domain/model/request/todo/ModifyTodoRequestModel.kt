package com.poptato.domain.model.request.todo

data class ModifyTodoRequestModel(
    val content: TodoContentModel,
    val todoId: Long
)
