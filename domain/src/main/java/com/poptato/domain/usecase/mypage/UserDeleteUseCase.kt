package com.poptato.domain.usecase.mypage

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.auth.UserDeleteRequestModel
import com.poptato.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDeleteUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) : UseCase<UserDeleteRequestModel, Result<Unit>>() {

    override suspend fun invoke(request: UserDeleteRequestModel): Flow<Result<Unit>> {
        return myPageRepository.userDelete(request)
    }
}