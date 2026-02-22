package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.mypage.PolicyResponse
import com.poptato.domain.model.response.mypage.PolicyModel

object PolicyResponseMapper : Mapper<PolicyResponse, PolicyModel> {

    override fun responseToModel(response: PolicyResponse?): PolicyModel {
        return response?.let { data ->
            PolicyModel(
                id = data.id,
                content = data.content,
                createdAt = data.createdAt
            )
        } ?: PolicyModel()
    }
}