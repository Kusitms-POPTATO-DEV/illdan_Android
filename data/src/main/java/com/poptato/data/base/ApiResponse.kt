package com.poptato.data.base

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("status")
    val status: Int = -1,

    @SerializedName("code")
    val code: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("result")
    val result: T? = null,

    @SerializedName("timestamp")
    val timestamp: String = ""
)