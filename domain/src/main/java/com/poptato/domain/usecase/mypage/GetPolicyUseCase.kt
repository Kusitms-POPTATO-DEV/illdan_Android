package com.poptato.domain.usecase.mypage

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.response.mypage.PolicyModel
import com.poptato.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPolicyUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
): UseCase<Unit, Result<PolicyModel>>() {

    override suspend fun invoke(request: Unit): Flow<Result<PolicyModel>> {
        return myPageRepository.getPolicy()
    }
}