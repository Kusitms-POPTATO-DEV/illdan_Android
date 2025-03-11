package com.poptato.domain.usecase

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.KaKaoLoginRequest
import com.poptato.domain.model.response.login.AuthModel
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostKaKaoLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<KaKaoLoginRequest, Result<AuthModel>>() {
    override suspend fun invoke(request: KaKaoLoginRequest): Flow<Result<AuthModel>> {
        return authRepository.login(request)
    }
}