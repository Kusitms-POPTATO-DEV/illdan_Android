package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.yesterday.YesterdayListResponse
import com.poptato.domain.model.response.yesterday.YesterdayItemModel
import com.poptato.domain.model.response.yesterday.YesterdayListModel

object YesterdayListResponseMapper : Mapper<YesterdayListResponse, YesterdayListModel> {

    override fun responseToModel(response: YesterdayListResponse?): YesterdayListModel {
        return response?.let { data ->
            YesterdayListModel(
                totalPageCount = data.totalPageCount,
                yesterdays = data.yesterdays.map { listItem ->
                    YesterdayItemModel(
                        todoId = listItem.todoId,
                        content = listItem.content,
                        bookmark = listItem.bookmark,
                        dday = listItem.dday,
                        repeat = listItem.repeat
                    )
                }
            )
        } ?: YesterdayListModel()
    }
}