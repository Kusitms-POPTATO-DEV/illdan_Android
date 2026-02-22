package com.poptato.domain.usecase.auth

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.response.auth.TokenModel
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<TokenModel, Result<Unit>>() {
    override suspend fun invoke(request: TokenModel): Flow<Result<Unit>> {
        return authRepository.saveToken(request)
    }
}