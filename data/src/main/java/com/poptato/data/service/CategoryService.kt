package com.poptato.data.service

import com.poptato.data.base.ApiResponse
import com.poptato.data.base.Endpoints
import com.poptato.data.model.response.category.CategoryIconTotalListResponse
import com.poptato.data.model.response.category.CategoryIdResponse
import com.poptato.data.model.response.category.CategoryListResponse
import com.poptato.domain.model.request.category.CategoryDragDropRequestModel
import com.poptato.domain.model.request.category.CategoryRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {

    @GET(Endpoints.Category.ICONS)
    suspend fun getCategoryIcon(): Response<ApiResponse<CategoryIconTotalListResponse>>

    @POST(Endpoints.Category.CATEGORY)
    suspend fun createCategory(
        @Body request: CategoryRequestModel
    ): Response<ApiResponse<CategoryIdResponse>>

    @GET(Endpoints.Category.LIST)
    suspend fun getCategoryList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ApiResponse<CategoryListResponse>>

    @PUT(Endpoints.Category.MODIFY)
    suspend fun modifyCategory(
        @Path("categoryId") category: Long,
        @Body request: CategoryRequestModel
    ): Response<ApiResponse<Unit>>

    @DELETE(Endpoints.Category.MODIFY)
    suspend fun deleteCategory(
        @Path("categoryId") category: Long,
    ): Response<ApiResponse<Unit>>

    @PATCH(Endpoints.Category.DRAG_DROP)
    suspend fun dragDrop(
        @Body request: CategoryDragDropRequestModel
    ): Response<ApiResponse<Unit>>
}