package com.poptato.domain.usecase.mypage

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.auth.LogoutRequestModel
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<LogoutRequestModel, Result<Unit>>() {
    override suspend fun invoke(request: LogoutRequestModel): Flow<Result<Unit>> {
        return authRepository.logout(request)
    }
}