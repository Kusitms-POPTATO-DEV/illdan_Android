package com.poptato.domain.usecase.mypage

import com.poptato.domain.base.UseCase
import com.poptato.domain.model.request.mypage.UserCommentRequest
import com.poptato.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendCommentUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) : UseCase<UserCommentRequest, Result<Unit>>() {
    override suspend fun invoke(request: UserCommentRequest): Flow<Result<Unit>> {
        return myPageRepository.sendComment(request)
    }
}