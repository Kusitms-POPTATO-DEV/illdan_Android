package com.poptato.domain.usecase.yesterday

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.todo.TodoIdsModel
import com.poptato.domain.repository.YesterdayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateYesterdayTodoCompletionUseCase @Inject constructor(
    private val yesterdayRepository: YesterdayRepository
) : UseCase<TodoIdsModel, Result<Unit>>() {
    override suspend fun invoke(request: TodoIdsModel): Flow<Result<Unit>> {
        return yesterdayRepository.updateYesterdayTodoCompletion(request)
    }
}