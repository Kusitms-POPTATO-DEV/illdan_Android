package com.poptato.category

import com.poptato.domain.model.enums.CategoryScreenType
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.ui.base.PageState

data class CategoryPageState(
    val screenType: CategoryScreenType = CategoryScreenType.Add,
    val categoryName: String = "",
    val categoryIconImgUrl: String = "",
    val categoryIconList: CategoryIconTotalListModel = CategoryIconTotalListModel(),
    val selectedIcon: CategoryIconItemModel? = null,
    val modifyCategoryId: Long = -1
): PageState