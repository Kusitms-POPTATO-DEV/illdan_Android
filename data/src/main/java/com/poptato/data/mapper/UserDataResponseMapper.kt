package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.mypage.UserDataResponse
import com.poptato.domain.model.response.mypage.UserDataModel

object UserDataResponseMapper : Mapper<UserDataResponse, UserDataModel> {

    override fun responseToModel(response: UserDataResponse?): UserDataModel {
        return response?.let { data ->
            UserDataModel(
                name = data.name ?: "",
                email = data.email ?: "",
                userImg = data.imageUrl ?: ""
            )
        } ?: UserDataModel()
    }
}