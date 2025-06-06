package com.poptato.domain.model.request.todo

import com.poptato.domain.model.enums.TodoType

data class DragDropRequestModel(
    val type: TodoType,
    val todoIds: List<Long>
)
