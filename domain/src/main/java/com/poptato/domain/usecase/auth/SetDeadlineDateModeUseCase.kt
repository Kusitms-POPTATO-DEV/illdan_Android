package com.poptato.domain.usecase.auth

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetDeadlineDateModeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<Boolean, Unit>() {
    override suspend fun invoke(request: Boolean): Flow<Unit> = flow {
        authRepository.setDeadlineDateMode(request)
    }
}