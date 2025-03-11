package com.poptato.mypage.policy

import com.poptato.domain.model.response.mypage.PolicyModel
import com.poptato.ui.base.PageState

data class PolicyPageState (
    val policyModel: PolicyModel = PolicyModel()
): PageState