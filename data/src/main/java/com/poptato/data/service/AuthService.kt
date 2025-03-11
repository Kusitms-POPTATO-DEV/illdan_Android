package com.poptato.data.service

import com.poptato.data.base.ApiResponse
import com.poptato.data.base.Endpoints
import com.poptato.data.model.response.auth.TokenResponse
import com.poptato.data.model.response.login.AuthResponse
import com.poptato.domain.model.request.KaKaoLoginRequest
import com.poptato.domain.model.request.ReissueRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST(Endpoints.Auth.LOGIN)
    suspend fun login(
        @Body request: KaKaoLoginRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST(Endpoints.Auth.REFRESH)
    suspend fun reissueToken(
        @Body request: ReissueRequestModel
    ): Response<ApiResponse<TokenResponse>>

    @POST(Endpoints.Auth.LOGOUT)
    suspend fun logout(): Response<ApiResponse<String>>
}