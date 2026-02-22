package com.poptato.domain.repository

import com.poptato.domain.model.request.category.CategoryDragDropRequestModel
import com.poptato.domain.model.request.category.CategoryIdModel
import com.poptato.domain.model.request.category.CategoryRequestModel
import com.poptato.domain.model.request.category.ModifyCategoryRequestModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryListModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategoryIcon(): Flow<Result<CategoryIconTotalListModel>>
    suspend fun createCategory(request: CategoryRequestModel): Flow<Result<CategoryIdModel>>
    suspend fun getCategoryList(page: Int, size: Int): Flow<Result<CategoryListModel>>
    suspend fun modifyCategory(request: ModifyCategoryRequestModel): Flow<Result<Unit>>
    suspend fun deleteCategory(categoryId: Long): Flow<Result<Unit>>
    suspend fun dragDrop(request: CategoryDragDropRequestModel): Flow<Result<Unit>>
}