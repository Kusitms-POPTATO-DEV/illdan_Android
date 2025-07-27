package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodoCompletionCountUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<Unit, Int>() {
    override suspend fun invoke(request: Unit): Flow<Int> {
        return todoRepository.getTodoCompletionCount()
    }
}