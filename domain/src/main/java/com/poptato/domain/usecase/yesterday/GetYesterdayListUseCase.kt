package com.poptato.domain.usecase.yesterday

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.ListRequestModel
import com.poptato.domain.model.response.yesterday.YesterdayListModel
import com.poptato.domain.repository.YesterdayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetYesterdayListUseCase @Inject constructor(
    private val yesterdayRepository: YesterdayRepository
) : UseCase<ListRequestModel, Result<YesterdayListModel>>() {

    override suspend fun invoke(request: ListRequestModel): Flow<Result<YesterdayListModel>> {
        return yesterdayRepository.getYesterdayList(page = request.page, size = request.size)
    }
}