package com.poptato.data.model.response.login

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("refreshToken") val refreshToken: String = "",
    @SerializedName("isNewUser") val isNewUser: Boolean = false,
    @SerializedName("userId") val userId: Int = -1
)
