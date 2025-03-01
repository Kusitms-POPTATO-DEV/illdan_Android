package com.poptato.domain.repository

import com.poptato.domain.model.response.yesterday.YesterdayListModel
import kotlinx.coroutines.flow.Flow

interface YesterdayRepository {

    suspend fun getYesterdayList(page: Int, size: Int): Flow<Result<YesterdayListModel>>
    suspend fun getShouldShowYesterday(): Flow<Boolean>
    suspend fun setShouldShowYesterday(value: Boolean)
}