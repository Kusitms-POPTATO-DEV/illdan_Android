package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.today.TodayListResponse
import com.poptato.domain.model.enums.TodoStatus
import com.poptato.domain.model.response.today.TodayListModel
import com.poptato.domain.model.response.today.TodoItemModel

object TodayListResponseMapper: Mapper<TodayListResponse, TodayListModel> {
    override fun responseToModel(response: TodayListResponse?): TodayListModel {
        return response?.let {
            TodayListModel(
                date = it.date,
                todays = response.todays.map { item ->
                    TodoItemModel(
                        todoId = item.todoId,
                        content = item.content,
                        todoStatus = if (item.todoStatus == "COMPLETED") TodoStatus.COMPLETED else TodoStatus.INCOMPLETE,
                        isBookmark = item.isBookmark,
                        isRepeat = item.isRepeat,
                        deadline = item.deadline ?: "",
                        dDay = item.dDay
                    )
                },
                totalPageCount = it.totalPageCount
            )
        } ?: TodayListModel()
    }
}