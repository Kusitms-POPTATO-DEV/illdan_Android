package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.mapper.TodayListResponseMapper
import com.poptato.data.service.TodayService
import com.poptato.domain.model.request.today.GetTodayListRequestModel
import com.poptato.domain.model.response.today.TodayListModel
import com.poptato.domain.repository.TodayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodayRepositoryImpl @Inject constructor(
    private val todayService: TodayService
) : TodayRepository, BaseRepository() {
    override suspend fun getTodayList(request: GetTodayListRequestModel): Flow<Result<TodayListModel>> {
        return apiLaunch(apiCall = { todayService.getTodayList(page = request.page, size = request.size) }, TodayListResponseMapper)
    }
}