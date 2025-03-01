package com.poptato.mypage

import com.poptato.domain.model.response.mypage.UserDataModel
import com.poptato.ui.base.PageState

data class MyPagePageState(
    val userDataModel: UserDataModel = UserDataModel(),
    val noticeWebViewState: Boolean = false,
    val faqWebViewState: Boolean = false,
    val policyViewState: Boolean = false
) : PageState