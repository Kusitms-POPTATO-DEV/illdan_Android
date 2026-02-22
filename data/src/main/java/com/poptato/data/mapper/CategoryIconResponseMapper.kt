package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.category.CategoryIconTotalListResponse
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryIconTypeListModel

object CategoryIconResponseMapper : Mapper<CategoryIconTotalListResponse, CategoryIconTotalListModel> {
    override fun responseToModel(response: CategoryIconTotalListResponse?): CategoryIconTotalListModel {
        return response?.let {
            CategoryIconTotalListModel(
                icons = it.groupEmojis.map { typeItem ->
                    CategoryIconTypeListModel(
                        iconType = typeItem.key,
                        icons = typeItem.value.map { item ->
                            CategoryIconItemModel(
                                iconId = item.emojiId,
                                iconImgUrl = item.imageUrl
                            )
                        }
                    )
                }
            )
        } ?: CategoryIconTotalListModel()
    }
}