package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.auth.TokenResponse
import com.poptato.domain.model.response.auth.TokenModel

object ReissueResponseMapper: Mapper<TokenResponse, TokenModel> {
    override fun responseToModel(response: TokenResponse?): TokenModel {
        return response?.let {
            TokenModel(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken
            )
        } ?: TokenModel()
    }
}