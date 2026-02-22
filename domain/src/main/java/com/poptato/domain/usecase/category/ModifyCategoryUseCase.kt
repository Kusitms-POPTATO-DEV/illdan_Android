package com.poptato.domain.usecase.category

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.category.ModifyCategoryRequestModel
import com.poptato.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModifyCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
): UseCase<ModifyCategoryRequestModel, Result<Unit>>() {
    override suspend fun invoke(request: ModifyCategoryRequestModel): Flow<Result<Unit>> {
        return categoryRepository.modifyCategory(request)
    }
}