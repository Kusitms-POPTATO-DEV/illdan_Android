package com.poptato.domain.usecase.yesterday

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.YesterdayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetShouldShowYesterdayUseCase @Inject constructor(
    private val yesterdayRepository: YesterdayRepository
) : UseCase<Boolean, Unit>() {
    override suspend fun invoke(request: Boolean): Flow<Unit> = flow {
        yesterdayRepository.setShouldShowYesterday(request)
        emit(Unit)
    }
}