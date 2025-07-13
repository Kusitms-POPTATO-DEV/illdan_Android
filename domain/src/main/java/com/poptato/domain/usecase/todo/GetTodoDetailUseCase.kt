package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.response.todo.TodoDetailItemModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodoDetailUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<Long, Result<TodoDetailItemModel>>() {
    override suspend fun invoke(request: Long): Flow<Result<TodoDetailItemModel>> {
        return todoRepository.getTodoDetail(request)
    }
}