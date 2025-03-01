package com.poptato.domain.usecase.auth

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.ReissueRequestModel
import com.poptato.domain.model.response.auth.TokenModel
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReissueTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) : UseCase<ReissueRequestModel, Result<TokenModel>>() {
    override suspend fun invoke(request: ReissueRequestModel): Flow<Result<TokenModel>> {
        return authRepository.reissueToken(request)
    }
}