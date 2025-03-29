package com.poptato.data.service

import com.poptato.data.base.ApiResponse
import com.poptato.data.base.Endpoints
import com.poptato.data.model.response.mypage.PolicyResponse
import com.poptato.data.model.response.mypage.UserDataResponse
import com.poptato.domain.model.request.auth.UserDeleteRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface MyPageService {

    @POST(Endpoints.MyPage.USER_DELETE)
    suspend fun userDelete(
        @Body request: UserDeleteRequestModel
    ): Response<ApiResponse<Unit>>

    @GET(Endpoints.MyPage.USER_DATA)
    suspend fun getUserData(): Response<ApiResponse<UserDataResponse>>

    @GET(Endpoints.MyPage.POLICY)
    suspend fun getPolicy(): Response<ApiResponse<PolicyResponse>>
}