package com.poptato.setting.servicedelete

import com.poptato.domain.model.enums.UserDeleteType
import com.poptato.ui.base.PageState

data class ServiceDeletePageState (
    val userName: String = "",
    val selectedReasonList: List<UserDeleteType> = emptyList(),
    val deleteInputReason: String = ""
): PageState