package com.poptato.mypage

import androidx.lifecycle.viewModelScope
import com.poptato.domain.model.response.mypage.UserDataModel
import com.poptato.domain.usecase.mypage.GetUserDataUseCase
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase
) : BaseViewModel<MyPagePageState>(
    MyPagePageState()
) {

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            getUserDataUseCase(request = Unit).collect {
                resultResponse(it, { data ->
                    setMappingToUserData(data)
                    Timber.d("[마이페이지] 유저 정보 서버통신 성공 -> $data")
                }, { error ->
                    Timber.d("[마이페이지] 유저 정보 서버통신 실패 -> $error")
                })
            }
        }
    }

    private fun setMappingToUserData(response: UserDataModel) {
        updateState(
            uiState.value.copy(
                userDataModel = response
            )
        )
    }

    fun updateState(state: Boolean, type: String) {
        when (type) {
            NOTICE_TYPE -> updateState(uiState.value.copy(noticeWebViewState = state))
            FAQ_TYPE -> updateState(uiState.value.copy(faqWebViewState = state))
            POLICY_TYPE -> updateState(uiState.value.copy(policyViewState = state))
        }
    }

    companion object {
        const val NOTICE_TYPE = "NOTICE"
        const val FAQ_TYPE = "FAQ"
        const val POLICY_TYPE = "POLICY"
    }
}