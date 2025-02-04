package com.poptato.di

import com.google.gson.Gson
import com.poptato.data.base.ApiResponse
import com.poptato.data.datastore.PoptatoDataStore
import com.poptato.domain.model.request.ReissueRequestModel
import com.poptato.domain.model.response.auth.TokenModel
import com.poptato.domain.repository.AuthRepository
import com.poptato.domain.usecase.auth.GetTokenUseCase
import com.poptato.domain.usecase.auth.ReissueTokenUseCase
import com.poptato.domain.usecase.auth.SaveTokenUseCase
import com.poptato.ui.util.CommonEventManager
import dagger.Lazy
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val getTokenUseCase: Lazy<GetTokenUseCase>,
    private val saveTokenUseCase: Lazy<SaveTokenUseCase>,
    private val reissueTokenUseCase: Lazy<ReissueTokenUseCase>
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val errorCode = response.parseErrorCode()

        return when (errorCode) {
            "AUTH-002" -> {
                val tokens = runBlocking {
                    getTokenUseCase.get().invoke(Unit).firstOrNull()
                }
                tokens?.let { handleTokenRefresh(response, it) }
            }
            "AUTH-008" -> {
                Timber.e("Token invalid: 유효하지 않은 토큰, 로그인 필요")
                runBlocking {
                    CommonEventManager.triggerLogout()
                }
                null
            }
            else -> {
                Timber.e("Unhandled error code: $errorCode")
                null
            }
        }
    }

    private fun handleTokenRefresh(response: Response, tokenModel: TokenModel): Request? {
        return runBlocking {
            try {
                val newTokensResult = reissueTokenUseCase.get().invoke(
                    ReissueRequestModel(
                        accessToken = tokenModel.accessToken,
                        refreshToken = tokenModel.refreshToken
                    )
                ).firstOrNull()

                newTokensResult?.getOrNull()?.let { newTokens ->
                    saveTokenUseCase.get().invoke(newTokens).collect {}
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${newTokens.accessToken}")
                        .build()
                } ?: run {
                    Timber.e("Token reissue failed: 새로운 토큰 없음")
                    null
                }
            } catch (e: Exception) {
                Timber.e("Token reissue failed: ${e.message}")
                null
            }
        }
    }

    private fun Response.parseErrorCode(): String {
        return try {
            val errorBody = this.peekBody(Long.MAX_VALUE).charStream()
            val apiResponse = Gson().fromJson(errorBody, ApiResponse::class.java) as ApiResponse<*>
            apiResponse.code
        } catch (e: Exception) {
            Timber.e("알 수 없는 에러 코드: ${e.message}")
            ""
        }
    }
}