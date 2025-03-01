package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.category.CategoryListResponse
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryListModel

object CategoryListResponseMapper: Mapper<CategoryListResponse, CategoryListModel> {
    override fun responseToModel(response: CategoryListResponse?): CategoryListModel {
        return response?.let { data ->
            CategoryListModel(
                categoryList = data.categories.map { item ->
                    CategoryItemModel(
                        categoryId = item.id,
                        iconId = item.emojiId,
                        categoryName = item.name,
                        categoryImgUrl = item.imageUrl
                    )
                }
            )
        } ?: CategoryListModel()
    }
}