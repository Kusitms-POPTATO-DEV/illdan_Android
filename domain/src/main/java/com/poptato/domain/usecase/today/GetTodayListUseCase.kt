package com.poptato.domain.usecase.today

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.today.GetTodayListRequestModel
import com.poptato.domain.model.response.today.TodayListModel
import com.poptato.domain.repository.TodayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayListUseCase @Inject constructor(
    private val todayRepository: TodayRepository
) : UseCase<GetTodayListRequestModel, Result<TodayListModel>>() {
    override suspend fun invoke(request: GetTodayListRequestModel): Flow<Result<TodayListModel>> {
        return todayRepository.getTodayList(request)
    }
}