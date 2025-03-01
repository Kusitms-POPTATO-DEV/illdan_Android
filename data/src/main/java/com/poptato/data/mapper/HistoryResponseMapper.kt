package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.history.HistoryListResponse
import com.poptato.domain.model.response.history.HistoryItemModel
import com.poptato.domain.model.response.history.HistoryListModel

object HistoryResponseMapper : Mapper<HistoryListResponse, HistoryListModel> {
    override fun responseToModel(response: HistoryListResponse?): HistoryListModel {
        return response?.let {
            HistoryListModel(
                histories = it.histories.map { item ->
                    HistoryItemModel(
                        todoId = item.todoId,
                        content = item.content,
                        date = item.date
                    )
                },
                totalPageCount = it.totalPageCount
            )
        } ?: HistoryListModel()
    }
}