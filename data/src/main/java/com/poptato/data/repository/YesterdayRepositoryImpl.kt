package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.datastore.PoptatoDataStore
import com.poptato.data.mapper.UnitResponseMapper
import com.poptato.data.mapper.YesterdayListResponseMapper
import com.poptato.data.service.YesterdayService
import com.poptato.domain.model.request.todo.TodoIdsModel
import com.poptato.domain.model.response.yesterday.YesterdayListModel
import com.poptato.domain.repository.YesterdayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class YesterdayRepositoryImpl @Inject constructor(
    private val yesterdayService: YesterdayService,
    private val dataStore: PoptatoDataStore
) : YesterdayRepository, BaseRepository() {

    override suspend fun getYesterdayList(page: Int, size: Int): Flow<Result<YesterdayListModel>> {
        return apiLaunch(
            apiCall = { yesterdayService.getYesterdayList(page, size) },
            YesterdayListResponseMapper
        )
    }

    override suspend fun getShouldShowYesterday(): Flow<Boolean> {
        return dataStore.shouldShowYesterday
    }

    override suspend fun setShouldShowYesterday(value: Boolean) {
        dataStore.setShouldShowYesterday(value)
    }

    override suspend fun updateYesterdayTodoCompletion(request: TodoIdsModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { yesterdayService.updateYesterdayTodoCompletion(request) }, UnitResponseMapper)
    }
}