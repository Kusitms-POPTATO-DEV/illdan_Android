package com.poptato.domain.usecase.history

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.history.HistoryListRequestModel
import com.poptato.domain.model.response.history.HistoryListModel
import com.poptato.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) : UseCase<HistoryListRequestModel, Result<HistoryListModel>>() {
    override suspend fun invoke(request: HistoryListRequestModel): Flow<Result<HistoryListModel>> {
        return historyRepository.getHistoryList(page = request.page, size = request.size, date = request.date)
    }
}