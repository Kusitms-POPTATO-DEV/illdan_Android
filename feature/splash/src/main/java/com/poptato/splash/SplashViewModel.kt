package com.poptato.splash

import androidx.lifecycle.viewModelScope
import com.poptato.domain.base.ApiException
import com.poptato.domain.model.request.ReissueRequestModel
import com.poptato.domain.model.request.today.GetTodayListRequestModel
import com.poptato.domain.model.response.auth.TokenModel
import com.poptato.domain.model.response.today.TodayListModel
import com.poptato.domain.usecase.auth.GetTokenUseCase
import com.poptato.domain.usecase.auth.ReissueTokenUseCase
import com.poptato.domain.usecase.auth.SaveTokenUseCase
import com.poptato.domain.usecase.today.GetTodayListUseCase
import com.poptato.ui.base.BaseViewModel
import com.poptato.ui.util.FCMManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val reissueTokenUseCase: ReissueTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : BaseViewModel<SplashPageState>(SplashPageState()) {
    init {
        checkLocalToken()
    }

    private fun checkLocalToken() {
        viewModelScope.launch {
            val token = getTokenUseCase(Unit).firstOrNull()

            if (token != null &&
                token.accessToken.isNotEmpty() &&
                token.refreshToken.isNotEmpty()
            ) {
                reissueToken(token)
            } else {
                updateState(uiState.value.copy(skipLogin = false))
            }
        }
    }

    private fun reissueToken(token: TokenModel) {
        FCMManager.getFCMToken { clientId ->
            viewModelScope.launch(Dispatchers.Main) {
                reissueTokenUseCase(
                    request = ReissueRequestModel(
                        accessToken = token.accessToken,
                        refreshToken = token.refreshToken,
                        clientId = clientId ?: "",
                        mobileType = "ANDROID"
                    )
                ).collect {
                    resultResponse(it, ::onSuccessReissueToken)
                }
            }
        }
    }

    private fun onSuccessReissueToken(response: TokenModel) {
        viewModelScope.launch(Dispatchers.Main) {
            saveTokenUseCase(request = response).collect{}
        }
        updateState(uiState.value.copy(skipLogin = true))
    }
}