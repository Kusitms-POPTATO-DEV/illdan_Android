package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.DragDropRequestModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DragDropUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<DragDropRequestModel, Result<Unit>>() {
    override suspend fun invoke(request: DragDropRequestModel): Flow<Result<Unit>> {
        return todoRepository.dragDrop(request)
    }
}