package com.poptato.domain.repository

import com.poptato.domain.model.request.today.GetTodayListRequestModel
import com.poptato.domain.model.response.today.TodayListModel
import kotlinx.coroutines.flow.Flow

interface TodayRepository {
    suspend fun getTodayList(request: GetTodayListRequestModel): Flow<Result<TodayListModel>>
}