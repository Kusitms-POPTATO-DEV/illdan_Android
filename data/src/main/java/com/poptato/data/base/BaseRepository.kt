package com.poptato.data.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poptato.domain.base.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.Reader

abstract class BaseRepository {

    inline fun <reified D, M> apiLaunch(
        crossinline apiCall: suspend () -> Response<ApiResponse<D>>,
        responseMapper: Mapper<D, M>
    ): Flow<Result<M>> = flow {

        val response = apiCall()

        when(response.isSuccessful) {
            true -> {
                val apiResponse = response.body() as ApiResponse
                val data = responseMapper.responseToModel(apiResponse.result)

                emit(Result.success(data))
            }
            false -> {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = fromGson<D>(errorBody)
                emit(
                    Result.failure(ApiException(errorResponse.code, errorResponse.message))
                )
            }
        }
    }

    inline fun <reified T> fromGson(json: Reader?): ApiResponse<T> {
        return Gson().fromJson(json, object : TypeToken<ApiResponse<T>>() {}.type) ?: ApiResponse()
    }
}