package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.ModifyTodoRequestModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModifyTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<ModifyTodoRequestModel, Result<Unit>>() {
    override suspend fun invoke(request: ModifyTodoRequestModel): Flow<Result<Unit>> {
        return todoRepository.modifyTodo(request)
    }
}