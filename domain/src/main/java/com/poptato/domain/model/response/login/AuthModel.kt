package com.poptato.domain.model.response.login

data class AuthModel(
    val accessToken: String = "",
    val refreshToken: String = "",
    val isNewUser: Boolean = false,
    val userId: Int = -1
)