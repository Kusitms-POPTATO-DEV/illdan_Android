package com.poptato.domain.usecase.auth

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.response.auth.TokenModel
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<Unit, TokenModel>() {
    override suspend fun invoke(request: Unit): Flow<TokenModel> {
        return authRepository.getToken()
    }
}