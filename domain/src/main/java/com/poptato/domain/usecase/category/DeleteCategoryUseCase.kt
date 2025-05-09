package com.poptato.domain.usecase.category

import com.poptato.domain.base.UseCase
import com.poptato.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
): UseCase<Long, Result<Unit>>() {
    override suspend fun invoke(request: Long): Flow<Result<Unit>> {
        return categoryRepository.deleteCategory(request)
    }
}