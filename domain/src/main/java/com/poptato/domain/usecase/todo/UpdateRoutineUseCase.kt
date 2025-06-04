package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.RoutineRequestModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateRoutineUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<UpdateRoutineUseCase.Companion.UpdateTodoRoutineModel, Result<Unit>>() {
    
    override suspend fun invoke(request: UpdateTodoRoutineModel): Flow<Result<Unit>> {
        return todoRepository.updateTodoRoutine(request.todoId, request.requestModel)
    }

    companion object {
        data class UpdateTodoRoutineModel(
            val todoId: Long,
            val requestModel: RoutineRequestModel
        )
    }
}