package com.poptato.data.service

import com.poptato.data.base.ApiResponse
import com.poptato.data.base.Endpoints
import com.poptato.data.model.response.today.TodayListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TodayService {
    @GET(Endpoints.Today.TODAYS)
    suspend fun getTodayList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ApiResponse<TodayListResponse>>
}