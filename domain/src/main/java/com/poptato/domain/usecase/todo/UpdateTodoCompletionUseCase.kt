package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateTodoCompletionUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<Long, Result<Unit>>() {
    override suspend fun invoke(request: Long): Flow<Result<Unit>> {
        return todoRepository.updateTodoCompletion(request)
    }
}