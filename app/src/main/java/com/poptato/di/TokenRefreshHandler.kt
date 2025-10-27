package com.poptato.di

import com.poptato.domain.model.request.ReissueRequestModel
import com.poptato.domain.usecase.auth.GetTokenUseCase
import com.poptato.domain.usecase.auth.ReissueTokenUseCase
import com.poptato.domain.usecase.auth.SaveTokenUseCase
import com.poptato.ui.util.FCMManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TokenRefreshHandler @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val reissueTokenUseCase: ReissueTokenUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {
    fun reissueToken() {
        coroutineScope.launch {
            try {
                val tokens = getTokenUseCase(Unit).firstOrNull()
                val clientId = FCMManager.getFCMTokenSuspend()

                tokens?.let {
                    val newTokens = reissueTokenUseCase(
                        request = ReissueRequestModel(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken,
                            clientId = clientId ?: "",
                            mobileType = "ANDROID"
                        )
                    ).firstOrNull()

                    newTokens?.getOrNull()?.let { newToken ->
                        saveTokenUseCase(newToken).collect()
                    } ?: Timber.e("토큰 갱신 실패")
                } ?: Timber.e("갱신할 토큰이 존재하지 않음")
            } catch (e: Exception) {
                Timber.e(e, "예외 발생")
            }

        }
    }
}