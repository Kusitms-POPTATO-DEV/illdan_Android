package com.poptato.domain.usecase.auth

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDeadlineDateModeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<Unit, Boolean>() {
    override suspend fun invoke(request: Unit): Flow<Boolean> {
        return authRepository.getDeadlineDateMode()
    }
}