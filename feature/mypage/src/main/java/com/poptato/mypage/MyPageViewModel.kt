package com.poptato.mypage

import androidx.lifecycle.viewModelScope
import com.poptato.domain.model.request.mypage.UserCommentRequest
import com.poptato.domain.model.response.mypage.UserDataModel
import com.poptato.domain.usecase.auth.GetDeadlineDateModeUseCase
import com.poptato.domain.usecase.auth.SetDeadlineDateModeUseCase
import com.poptato.domain.usecase.mypage.GetUserDataUseCase
import com.poptato.domain.usecase.mypage.SendCommentUseCase
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getDeadlineDateModeUseCase: GetDeadlineDateModeUseCase,
    private val setDeadlineDateModeUseCase: SetDeadlineDateModeUseCase,
    private val sendCommentUseCase: SendCommentUseCase
) : BaseViewModel<MyPagePageState>(
    MyPagePageState()
) {

    init {
        getUserData()
        getDeadlineDateMode()
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

    private fun getDeadlineDateMode() {
        viewModelScope.launch {
            getDeadlineDateModeUseCase(Unit).collect {
                updateState(uiState.value.copy(isDeadlineDateMode = it))
            }
        }
    }

    fun setDeadlineDateMode(value: Boolean) {
        viewModelScope.launch {
            setDeadlineDateModeUseCase(value).collect()
        }
    }

    /**-------------------------------------------UserComment------------------------------------------------*/

    fun sendComment(comment: String, contact: String) {
        if (comment.isBlank()) return
        
        viewModelScope.launch {
            sendCommentUseCase(UserCommentRequest(comment, contact)).collect {
                resultResponse(it, ::onSuccessSendComment, ::onFailedSendComment)
            }
        }
    }

    private fun onSuccessSendComment(result: Unit) {
        emitEventFlow(MyPageEvent.SendCommentSuccess)
    }

    private fun onFailedSendComment(error: Throwable) {
        Timber.e("[마이페이지] 의견 전송 실패 -> $error")
    }

    companion object {
        const val NOTICE_TYPE = "NOTICE"
        const val FAQ_TYPE = "FAQ"
        const val POLICY_TYPE = "POLICY"
    }
}