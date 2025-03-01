package com.poptato.setting.userdata

import androidx.lifecycle.viewModelScope
import com.poptato.domain.model.response.mypage.UserDataModel
import com.poptato.domain.usecase.auth.ClearTokenUseCase
import com.poptato.domain.usecase.mypage.GetUserDataUseCase
import com.poptato.domain.usecase.mypage.LogOutUseCase
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val clearTokenUseCase: ClearTokenUseCase
) : BaseViewModel<UserDataPageState>(
    UserDataPageState()
) {

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            getUserDataUseCase(request = Unit).collect {
                resultResponse(it, ::setMappingToUserName)
            }
        }
    }

    private fun setMappingToUserName(response: UserDataModel) {
        updateState(
            uiState.value.copy(
                userDataModel = response
            )
        )
    }

    fun logOut() {
        viewModelScope.launch {
            logOutUseCase(request = Unit).collect {
                resultResponse(it, {
                    clearTokenUseCase
                    emitEventFlow(UserDataEvent.GoBackToLogIn)
                }, { error ->
                    Timber.d("[계정정보] 로그아웃 서버통신 실패 -> ${error.message}")
                })
            }
        }
    }
}