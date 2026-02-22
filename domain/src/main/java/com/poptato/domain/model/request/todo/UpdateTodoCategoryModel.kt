package com.poptato.domain.model.request.todo

data class UpdateTodoCategoryModel (
    val todoId: Long,
    val todoCategoryModel: TodoCategoryIdModel
)