package com.poptato.setting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.poptato.domain.usecase.auth.ClearTokenUseCase
import com.poptato.domain.usecase.mypage.LogOutUseCase
import com.poptato.setting.logout.LogOutDialogState
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val clearTokenUseCase: ClearTokenUseCase
): BaseViewModel<SettingPageState>(
    SettingPageState()
) {

    val logOutDialogState: MutableState<LogOutDialogState> = mutableStateOf(LogOutDialogState())

    init {

        logOutDialogState.value = LogOutDialogState(
            onDismissRequest = {
                logOutDialogState.value = logOutDialogState.value.copy(isShowDialog = false)
            },
            onClickBackBtn = {
                logOutDialogState.value = logOutDialogState.value.copy(isShowDialog = false)
            },
            onClickLogOutBtn = {
                logOut()
                logOutDialogState.value = logOutDialogState.value.copy(isShowDialog = false)
            },
        )
    }

    fun showLogOutDialog() {
        logOutDialogState.value = logOutDialogState.value.copy(isShowDialog = true)
    }

    private fun logOut() {
        viewModelScope.launch {
            logOutUseCase(request = Unit).collect {
                resultResponse(it, {
                    clearTokenUseCase
                    emitEventFlow(SettingEvent.GoBackToLogIn)
                }, { error ->
                    Timber.d("[마이페이지] 로그아웃 서버통신 실패 -> ${error.message}")
                })
            }
        }
    }
}