package com.poptato.domain.usecase.category

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.category.CategoryDragDropRequestModel
import com.poptato.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryDragDropUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) : UseCase<CategoryDragDropRequestModel, Result<Unit>>() {
    override suspend fun invoke(request: CategoryDragDropRequestModel): Flow<Result<Unit>> {
        return categoryRepository.dragDrop(request)
    }
}