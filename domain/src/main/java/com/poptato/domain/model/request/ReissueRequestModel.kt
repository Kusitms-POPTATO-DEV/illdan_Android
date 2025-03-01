package com.poptato.domain.model.request

data class ReissueRequestModel(
    val accessToken: String = "",
    val refreshToken: String = ""
)
