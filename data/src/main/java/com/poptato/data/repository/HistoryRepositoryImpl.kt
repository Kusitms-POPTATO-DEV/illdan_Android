package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.mapper.HistoryCalendarResponseMapper
import com.poptato.data.mapper.HistoryResponseMapper
import com.poptato.data.service.HistoryService
import com.poptato.domain.model.response.history.HistoryCalendarListModel
import com.poptato.domain.model.response.history.HistoryListModel
import com.poptato.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyService: HistoryService
): HistoryRepository, BaseRepository() {
    override suspend fun getHistoryList(page: Int, size: Int, date: String): Flow<Result<HistoryListModel>> {
        return apiLaunch(apiCall = { historyService.getHistoryList(page, size, date) }, HistoryResponseMapper)
    }

    override suspend fun getHistoryCalendarList(
        year: String,
        month: Int
    ): Flow<Result<HistoryCalendarListModel>> {
        return apiLaunch(apiCall = { historyService.getHistoryCalendarList(year, month) }, HistoryCalendarResponseMapper)
    }
}