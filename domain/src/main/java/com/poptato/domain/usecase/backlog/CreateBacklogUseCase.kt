package com.poptato.domain.usecase.backlog

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.backlog.CreateBacklogRequestModel
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.repository.BacklogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateBacklogUseCase @Inject constructor(
    private val backlogRepository: BacklogRepository
) : UseCase<CreateBacklogRequestModel, Result<TodoIdModel>>() {
    override suspend fun invoke(request: CreateBacklogRequestModel): Flow<Result<TodoIdModel>> {
        return backlogRepository.createBacklog(request)
    }
}