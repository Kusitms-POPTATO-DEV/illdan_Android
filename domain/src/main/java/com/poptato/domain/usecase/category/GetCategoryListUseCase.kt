package com.poptato.domain.usecase.category

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.category.GetCategoryListRequestModel
import com.poptato.domain.model.response.category.CategoryListModel
import com.poptato.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
): UseCase<GetCategoryListRequestModel, Result<CategoryListModel>>() {
    override suspend fun invoke(request: GetCategoryListRequestModel): Flow<Result<CategoryListModel>> {
        return categoryRepository.getCategoryList(page = request.page, size = request.size)
    }
}