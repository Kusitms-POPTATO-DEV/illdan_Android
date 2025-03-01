package com.poptato.domain.usecase.yesterday

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.YesterdayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShouldShowYesterdayUseCase @Inject constructor(
    private val yesterdayRepository: YesterdayRepository
) : UseCase<Unit, Boolean>() {
    override suspend fun invoke(request: Unit): Flow<Boolean> {
        return yesterdayRepository.getShouldShowYesterday()
    }
}