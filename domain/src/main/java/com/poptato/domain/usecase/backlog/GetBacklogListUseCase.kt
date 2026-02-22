package com.poptato.domain.usecase.backlog

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.backlog.GetBacklogListRequestModel
import com.poptato.domain.model.response.backlog.BacklogListModel
import com.poptato.domain.repository.BacklogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBacklogListUseCase @Inject constructor(
    private val backlogRepository: BacklogRepository
) : UseCase<GetBacklogListRequestModel, Result<BacklogListModel>>() {
    override suspend fun invoke(request: GetBacklogListRequestModel): Flow<Result<BacklogListModel>> {
        return backlogRepository.getBacklogList(categoryId = request.categoryId, page = request.page, size = request.size)
    }
}