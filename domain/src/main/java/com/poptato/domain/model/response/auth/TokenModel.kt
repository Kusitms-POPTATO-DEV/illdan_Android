package com.poptato.domain.model.response.auth

data class TokenModel(
    val accessToken: String = "",
    val refreshToken: String = ""
)
