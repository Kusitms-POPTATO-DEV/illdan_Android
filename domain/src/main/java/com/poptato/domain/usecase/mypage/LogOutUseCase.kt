package com.poptato.domain.usecase.mypage

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<Unit, Result<Unit>>() {

    override suspend fun invoke(request: Unit): Flow<Result<Unit>> {
        return authRepository.logout()
    }
}