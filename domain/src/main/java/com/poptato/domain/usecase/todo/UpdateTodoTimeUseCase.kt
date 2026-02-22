package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.TodoTimeModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateTodoTimeUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<UpdateTodoTimeUseCase.Companion.UpdateTodoTimeModel, Result<Unit>>() {

    override suspend fun invoke(request: UpdateTodoTimeModel): Flow<Result<Unit>> {
        return todoRepository.updateTodoTime(request.todoId, request.requestModel)
    }

    companion object {
        data class UpdateTodoTimeModel(
            val todoId: Long,
            val requestModel: TodoTimeModel
        )
    }
}