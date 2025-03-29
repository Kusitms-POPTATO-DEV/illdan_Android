package com.poptato.data.model.response.mypage

import com.google.gson.annotations.SerializedName

data class PolicyResponse (
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("content")
    val content: String = "",
    @SerializedName("createdAt")
    val createdAt: String = ""
)