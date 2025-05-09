package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.mapper.CategoryIconResponseMapper
import com.poptato.data.mapper.CategoryIdResponseMapper
import com.poptato.data.mapper.CategoryListResponseMapper
import com.poptato.data.mapper.UnitResponseMapper
import com.poptato.data.service.CategoryService
import com.poptato.domain.model.request.category.CategoryDragDropRequestModel
import com.poptato.domain.model.request.category.CategoryIdModel
import com.poptato.domain.model.request.category.CategoryRequestModel
import com.poptato.domain.model.request.category.ModifyCategoryRequestModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryListModel
import com.poptato.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryService: CategoryService
) : CategoryRepository, BaseRepository() {

    override suspend fun getCategoryIcon(): Flow<Result<CategoryIconTotalListModel>> {
        return apiLaunch(
            apiCall = { categoryService.getCategoryIcon() },
            CategoryIconResponseMapper
        )
    }

    override suspend fun createCategory(request: CategoryRequestModel): Flow<Result<CategoryIdModel>> {
        return apiLaunch(
            apiCall = { categoryService.createCategory(request) },
            CategoryIdResponseMapper
        )
    }

    override suspend fun getCategoryList(page: Int, size: Int): Flow<Result<CategoryListModel>> {
        return apiLaunch(
            apiCall = { categoryService.getCategoryList(page, size) },
            CategoryListResponseMapper
        )
    }

    override suspend fun modifyCategory(
        request: ModifyCategoryRequestModel
    ): Flow<Result<Unit>> {
        return apiLaunch(
            apiCall = { categoryService.modifyCategory(request.categoryId, request.categoryModel) },
            UnitResponseMapper
        )
    }

    override suspend fun deleteCategory(categoryId: Long): Flow<Result<Unit>> {
        return apiLaunch(
            apiCall = { categoryService.deleteCategory(categoryId) }, UnitResponseMapper
        )
    }

    override suspend fun dragDrop(request: CategoryDragDropRequestModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { categoryService.dragDrop(request) }, UnitResponseMapper)
    }
}