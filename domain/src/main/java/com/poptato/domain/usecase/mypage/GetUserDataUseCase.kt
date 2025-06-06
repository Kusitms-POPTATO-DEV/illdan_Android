package com.poptato.domain.usecase.mypage

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.response.mypage.UserDataModel
import com.poptato.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
): UseCase<Unit, Result<UserDataModel>>() {

    override suspend fun invoke(request: Unit): Flow<Result<UserDataModel>> {
        return myPageRepository.getUserData()
    }
}