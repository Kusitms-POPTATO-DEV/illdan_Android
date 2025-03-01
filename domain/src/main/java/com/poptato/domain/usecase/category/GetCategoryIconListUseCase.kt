package com.poptato.domain.usecase.category

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryIconListUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) : UseCase<Unit, Result<CategoryIconTotalListModel>>() {

    override suspend fun invoke(request: Unit): Flow<Result<CategoryIconTotalListModel>> {
        return categoryRepository.getCategoryIcon()
    }
}