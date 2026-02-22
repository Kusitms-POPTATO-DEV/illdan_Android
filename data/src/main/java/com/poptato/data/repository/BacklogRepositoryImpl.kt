package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.mapper.BacklogListResponseMapper
import com.poptato.data.mapper.TodoIdResponseMapper
import com.poptato.data.mapper.UnitResponseMapper
import com.poptato.data.service.BacklogService
import com.poptato.domain.model.request.backlog.CreateBacklogRequestModel
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.model.response.backlog.BacklogListModel
import com.poptato.domain.repository.BacklogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BacklogRepositoryImpl @Inject constructor(
    private val backlogService: BacklogService
): BacklogRepository, BaseRepository() {
    override suspend fun createBacklog(request :CreateBacklogRequestModel): Flow<Result<TodoIdModel>> {
        return apiLaunch(apiCall = { backlogService.createBacklog(request) }, TodoIdResponseMapper )
    }

    override suspend fun getBacklogList(categoryId: Long, page: Int, size: Int): Flow<Result<BacklogListModel>> {
        return apiLaunch(apiCall = { backlogService.getBacklogList(categoryId, page, size) }, BacklogListResponseMapper)
    }
}