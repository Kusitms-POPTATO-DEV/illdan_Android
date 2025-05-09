package com.poptato.login

import androidx.lifecycle.viewModelScope
import com.poptato.design_system.MOBILE_TYPE
import com.poptato.design_system.SOCIAL_TYPE
import com.poptato.domain.model.request.KaKaoLoginRequest
import com.poptato.domain.model.response.auth.TokenModel
import com.poptato.domain.model.response.login.AuthModel
import com.poptato.domain.usecase.PostKaKaoLoginUseCase
import com.poptato.domain.usecase.auth.SaveTokenUseCase
import com.poptato.ui.base.BaseViewModel
import com.poptato.ui.util.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KaKaoLoginViewModel @Inject constructor(
    private val postKaKaoLoginUseCase: PostKaKaoLoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : BaseViewModel<KaKaoLoginPageState>(
    KaKaoLoginPageState()
) {

    fun getClientId(fcmToken: String) {
        updateState(
            uiState.value.copy(
                clientId = fcmToken
            )
        )
    }

    fun kakaoLogin(token: String) {
        viewModelScope.launch {
            postKaKaoLoginUseCase(request = KaKaoLoginRequest(
                accessToken = token,
                clientId = uiState.value.clientId,
                socialType = SOCIAL_TYPE,
                mobileType = MOBILE_TYPE
            )).collect {
                resultResponse(it, ::onSuccessKaKaoLogin)
            }
        }
    }

    private fun onSuccessKaKaoLogin(model: AuthModel) {
        AnalyticsManager.logEvent(
            eventName = "log_in",
            params = mapOf("method" to "kakao")
        )
        viewModelScope.launch {
            saveTokenUseCase.invoke(
                request = TokenModel(accessToken = model.accessToken, refreshToken = model.refreshToken)
            ).collect {
                resultResponse(it, {})
            }
        }
        if (model.isNewUser) emitEventFlow(KaKaoLoginEvent.NewUserLogin)
        else emitEventFlow(KaKaoLoginEvent.OnSuccessLogin)
    }
}