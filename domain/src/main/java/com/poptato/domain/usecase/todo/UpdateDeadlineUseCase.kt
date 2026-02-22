package com.poptato.domain.usecase.todo

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.UpdateDeadlineRequestModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateDeadlineUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) : UseCase<UpdateDeadlineRequestModel, Result<Unit>>() {
    override suspend fun invoke(request: UpdateDeadlineRequestModel): Flow<Result<Unit>> {
        return todoRepository.updateDeadline(request)
    }
}