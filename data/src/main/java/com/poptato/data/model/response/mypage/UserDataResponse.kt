package com.poptato.data.model.response.mypage

import com.google.gson.annotations.SerializedName

data class UserDataResponse (
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("imageUrl")
    val imageUrl: String? = ""
)