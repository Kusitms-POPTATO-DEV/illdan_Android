package com.poptato.category

import androidx.lifecycle.viewModelScope
import com.poptato.domain.model.enums.CategoryScreenType
import com.poptato.domain.model.request.category.CategoryRequestModel
import com.poptato.domain.model.request.category.ModifyCategoryRequestModel
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.usecase.category.CreateCategoryUseCase
import com.poptato.domain.usecase.category.GetCategoryIconListUseCase
import com.poptato.domain.usecase.category.ModifyCategoryUseCase
import com.poptato.ui.base.BaseViewModel
import com.poptato.ui.util.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoryIconListUseCase: GetCategoryIconListUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val modifyCategoryUseCase: ModifyCategoryUseCase
): BaseViewModel<CategoryPageState>(
    CategoryPageState()
) {

    init {
        getCategoryIconList()
    }

    fun onValueChange(newValue: String) {
        updateState(
            uiState.value.copy(
                categoryName = newValue
            )
        )
    }

    private fun getCategoryIconList() {
        viewModelScope.launch {
            getCategoryIconListUseCase(request = Unit).collect {
                resultResponse(it, { data ->
                    onSuccessGetCategoryIconList(data)
                    Timber.d("[카테고리] 아이콘 리스트 서버통신 성공 -> $data")
                }, { error ->
                    Timber.d("[카테고리] 아이콘 리스트 서버통신 실패 -> ${error.message}")
                })
            }
        }
    }

    private fun onSuccessGetCategoryIconList(response: CategoryIconTotalListModel) {
        updateState(
            uiState.value.copy(
                categoryIconList = response
            )
        )
    }

    fun getSelectedIcon(icon: CategoryIconItemModel) {
        updateState(
            uiState.value.copy(
                selectedIcon = icon,
                categoryIconImgUrl = icon.iconImgUrl
            )
        )
    }

    fun getModifyIconItem(item: CategoryScreenContentModel) {
        updateState(
            uiState.value.copy(
                screenType = item.screenType,
                categoryName = item.categoryItem.categoryName,
                selectedIcon = CategoryIconItemModel(item.categoryItem.iconId, item.categoryItem.categoryImgUrl),
                categoryIconImgUrl = item.categoryItem.categoryImgUrl,
                modifyCategoryId = item.categoryItem.categoryId
            )
        )
    }

    fun finishSettingCategory() {
        if (uiState.value.screenType == CategoryScreenType.Add) {
            createCategory()
        } else {
            modifyCategory()
        }
    }

    private fun createCategory() {
        AnalyticsManager.logEvent(
            eventName = "make_category",
            params = mapOf("category_name" to uiState.value.categoryName)
        )

        viewModelScope.launch {
            createCategoryUseCase(request = CategoryRequestModel(uiState.value.categoryName, uiState.value.selectedIcon?.iconId ?: -1)).collect {
                resultResponse(it, {
                    emitEventFlow(CategoryEvent.GoToBacklog)
                }, { error ->
                    Timber.d("[카테고리] 카테고리 생성 서버통신 실패 -> $error")
                })
            }
        }
    }

    private fun modifyCategory() {
        viewModelScope.launch {
            modifyCategoryUseCase(request = ModifyCategoryRequestModel(uiState.value.modifyCategoryId, CategoryRequestModel(uiState.value.categoryName, uiState.value.selectedIcon?.iconId ?: -1))).collect {
                resultResponse(it, {
                    emitEventFlow(CategoryEvent.GoToBacklog)
                }, { error ->
                    Timber.d("[카테고리] 카테고리 수정 서버통신 실패 -> $error")
                })
            }
        }
    }
}