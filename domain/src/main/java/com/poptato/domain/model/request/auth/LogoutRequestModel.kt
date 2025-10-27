package com.poptato.domain.model.request.auth

data class LogoutRequestModel(
    val clientId: String = "",
    val mobileType: String = "ANDROID"
)