package com.poptato.domain.usecase.history

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.history.HistoryCalendarRequestModel
import com.poptato.domain.model.request.history.HistoryListRequestModel
import com.poptato.domain.model.response.history.HistoryCalendarListModel
import com.poptato.domain.model.response.history.HistoryListModel
import com.poptato.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryCalendarListUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) : UseCase<HistoryCalendarRequestModel, Result<HistoryCalendarListModel>>() {
    override suspend fun invoke(request: HistoryCalendarRequestModel): Flow<Result<HistoryCalendarListModel>> {
        return historyRepository.getHistoryCalendarList(year = request.year, month = request.month)
    }
}