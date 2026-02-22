package com.poptato.domain.model.request

data class KaKaoLoginRequest(
    val socialType: String = "",
    val accessToken: String = "",
    val mobileType: String = "",
    val clientId: String = ""
)
