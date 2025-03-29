package com.poptato.domain.model.request.auth

import com.poptato.domain.model.enums.UserDeleteType

data class UserDeleteRequestModel (
    val reasons: List<UserDeleteType>?,
    val userInputReason: String?
)