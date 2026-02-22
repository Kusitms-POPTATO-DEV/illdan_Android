package com.poptato.setting.servicedelete.finish

import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServiceDeleteFinishViewModel @Inject constructor(

): BaseViewModel<ServiceDeleteFinishPageState>(
    ServiceDeleteFinishPageState()
) {

    fun getDeleteUserName(name: String) {
        updateState(
            uiState.value.copy(
                userName = name
            )
        )
    }
}