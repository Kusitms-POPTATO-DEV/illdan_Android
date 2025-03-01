package com.poptato.domain.usecase.category

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.category.CategoryIdModel
import com.poptato.domain.model.request.category.CategoryRequestModel
import com.poptato.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
): UseCase<CategoryRequestModel, Result<CategoryIdModel>>() {
    override suspend fun invoke(request: CategoryRequestModel): Flow<Result<CategoryIdModel>> {
        return categoryRepository.createCategory(request)
    }
}