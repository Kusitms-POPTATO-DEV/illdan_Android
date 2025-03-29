package com.poptato.data.service

import com.poptato.data.base.ApiResponse
import com.poptato.data.base.Endpoints
import com.poptato.data.model.response.yesterday.YesterdayListResponse
import com.poptato.domain.model.request.todo.TodoIdsModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface YesterdayService {

    @GET(Endpoints.Yesterday.YESTERDAY)
    suspend fun getYesterdayList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ApiResponse<YesterdayListResponse>>

    @POST(Endpoints.Yesterday.YESTERDAY_COMPLETION)
    suspend fun updateYesterdayTodoCompletion(
        @Body request: TodoIdsModel
    ): Response<ApiResponse<Unit>>
}