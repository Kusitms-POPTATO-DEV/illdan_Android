package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.mapper.PolicyResponseMapper
import com.poptato.data.mapper.UnitResponseMapper
import com.poptato.data.mapper.UserDataResponseMapper
import com.poptato.data.service.MyPageService
import com.poptato.domain.model.request.auth.UserDeleteRequestModel
import com.poptato.domain.model.response.mypage.PolicyModel
import com.poptato.domain.model.response.mypage.UserDataModel
import com.poptato.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageService: MyPageService
) : MyPageRepository, BaseRepository() {

    override suspend fun userDelete(request: UserDeleteRequestModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { myPageService.userDelete(request) }, UnitResponseMapper)
    }

    override suspend fun getUserData(): Flow<Result<UserDataModel>> {
        return apiLaunch(apiCall = { myPageService.getUserData() }, UserDataResponseMapper)
    }

    override suspend fun getPolicy(): Flow<Result<PolicyModel>> {
        return apiLaunch(apiCall = { myPageService.getPolicy() }, PolicyResponseMapper)
    }
}