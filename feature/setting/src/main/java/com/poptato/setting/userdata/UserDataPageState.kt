package com.poptato.setting.userdata

import com.poptato.domain.model.response.mypage.UserDataModel
import com.poptato.ui.base.PageState

data class UserDataPageState (
    val userDataModel: UserDataModel = UserDataModel()
): PageState