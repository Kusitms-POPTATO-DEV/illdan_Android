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
    val modifyCategoryId: Long = -1,
    val categoryIndex: Int = -1,
    val currentSelectedIndex: Int = -1  // 백로그 -> 카테고리 화면으로 넘어오는 시점에서 선택된 인덱스
): PageState