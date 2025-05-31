package com.poptato.mypage.policy

import androidx.lifecycle.viewModelScope
import com.poptato.domain.model.response.mypage.PolicyModel
import com.poptato.domain.usecase.mypage.GetPolicyUseCase
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PolicyViewModel @Inject constructor(
    private val getPolicyUseCase: GetPolicyUseCase
) : BaseViewModel<PolicyPageState>(
    PolicyPageState()
) {

    init {
        getPolicy()
    }

    private fun getPolicy() {
        viewModelScope.launch {
            getPolicyUseCase(request = Unit).collect {
                resultResponse(it, { data ->
                    setMappingToPolicy(data)
                    Timber.d("[policy] policy -> ${data.content}")
                }
                )
            }
        }
    }

    private fun setMappingToPolicy(response: PolicyModel) {
        updateState(
            uiState.value.copy(
                policyModel = response
            )
        )
    }
}