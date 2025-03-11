package com.poptato.domain.repository

import com.poptato.domain.model.request.backlog.CreateBacklogRequestModel
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.model.response.backlog.BacklogListModel
import kotlinx.coroutines.flow.Flow

interface BacklogRepository {
    suspend fun createBacklog(request: CreateBacklogRequestModel): Flow<Result<TodoIdModel>>
    suspend fun getBacklogList(categoryId: Long, page: Int, size: Int): Flow<Result<BacklogListModel>>
}