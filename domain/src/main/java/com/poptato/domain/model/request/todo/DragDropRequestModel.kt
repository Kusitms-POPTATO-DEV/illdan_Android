package com.poptato.domain.model.request.todo

import com.poptato.core.enums.TodoType

data class DragDropRequestModel(
    val type: TodoType,
    val todoIds: List<Long>
)
