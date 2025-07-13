package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.login.AuthResponse
import com.poptato.domain.model.response.login.AuthModel

object AuthResponseMapper: Mapper<AuthResponse, AuthModel> {
    override fun responseToModel(response: AuthResponse?): AuthModel {
        return response?.let {
            AuthModel(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken,
                isNewUser = it.isNewUser,
                userId = it.userId
            )
        } ?: AuthModel()
    }
}