package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SwipeTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<TodoIdModel, Result<Unit>>() {
    override suspend fun invoke(request: TodoIdModel): Flow<Result<Unit>> {
        return todoRepository.swipeTodo(request)
    }
}