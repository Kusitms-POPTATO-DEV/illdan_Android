package com.poptato.test_utils.fake

import com.poptato.domain.base.UseCase
import kotlinx.coroutines.flow.Flow

abstract class FakeUseCase<P, R>(
    private val resultFlow: Flow<R>
) : UseCase<P, R>() {
    override suspend fun invoke(request: P): Flow<R> = resultFlow
}