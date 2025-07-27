package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetTodoCompletionCountUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<Int, Unit>() {
    override suspend fun invoke(request: Int): Flow<Unit> = flow {
        todoRepository.setTodoCompletionCount(request)
    }
}