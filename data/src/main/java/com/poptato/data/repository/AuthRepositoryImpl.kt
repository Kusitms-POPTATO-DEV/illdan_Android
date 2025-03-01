package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.datastore.PoptatoDataStore
import com.poptato.data.mapper.AuthLogOutResponseMapper
import com.poptato.data.mapper.AuthResponseMapper
import com.poptato.data.mapper.ReissueResponseMapper
import com.poptato.data.service.AuthService
import com.poptato.domain.model.request.KaKaoLoginRequest
import com.poptato.domain.model.request.ReissueRequestModel
import com.poptato.domain.model.response.auth.TokenModel
import com.poptato.domain.model.response.login.AuthModel
import com.poptato.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (
    private val authService: AuthService,
    private val dataStore: PoptatoDataStore
): AuthRepository, BaseRepository() {
    override suspend fun login(request: KaKaoLoginRequest): Flow<Result<AuthModel>> {
        return apiLaunch(apiCall = { authService.login(request) }, AuthResponseMapper)
    }

    override suspend fun reissueToken(request: ReissueRequestModel): Flow<Result<TokenModel>> {
        return apiLaunch(apiCall = { authService.reissueToken(request) }, ReissueResponseMapper )
    }

    override suspend fun saveToken(request: TokenModel): Flow<Result<Unit>> = flow {
        dataStore.saveAccessToken(request.accessToken)
        dataStore.saveRefreshToken(request.refreshToken)
    }

    override suspend fun clearToken(): Flow<Result<Unit>> = flow {
        dataStore.clearToken()
    }

    override suspend fun logout(): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { authService.logout() }, AuthLogOutResponseMapper)
    }

    override suspend fun getToken(): Flow<TokenModel> {
        return combine(
            dataStore.accessToken,
            dataStore.refreshToken
        ) { accessToken, refreshToken ->
            TokenModel(
                accessToken = accessToken.orEmpty(),
                refreshToken = refreshToken.orEmpty()
            )
        }
    }
}