package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.category.CategoryIdResponse
import com.poptato.domain.model.request.category.CategoryIdModel

object CategoryIdResponseMapper : Mapper<CategoryIdResponse, CategoryIdModel> {
    override fun responseToModel(response: CategoryIdResponse?): CategoryIdModel {
        return response?.let {
            CategoryIdModel(categoryId = it.categoryId)
        } ?: CategoryIdModel()
    }
}