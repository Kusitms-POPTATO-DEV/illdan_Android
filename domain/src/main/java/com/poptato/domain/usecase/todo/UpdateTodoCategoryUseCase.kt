package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.UpdateTodoCategoryModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateTodoCategoryUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<UpdateTodoCategoryModel, Result<Unit>>() {
    override suspend fun invoke(request: UpdateTodoCategoryModel): Flow<Result<Unit>> {
        return todoRepository.updateTodoCategory(request)
    }
}