package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.backlog.BacklogListResponse
import com.poptato.domain.model.response.backlog.BacklogListModel
import com.poptato.domain.model.response.today.TodoItemModel

object BacklogListResponseMapper: Mapper<BacklogListResponse, BacklogListModel> {
    override fun responseToModel(response: BacklogListResponse?): BacklogListModel {
        return response?.let {
            BacklogListModel(
                totalCount = it.totalCount,
                backlogs = it.backlogs.map { item ->
                    TodoItemModel(
                        todoId = item.todoId,
                        content = item.content,
                        isBookmark = item.isBookmark,
                        isRepeat = item.isRepeat,
                        deadline = item.deadline ?: "",
                        dDay = item.dDay
                    )
                },
                totalPageCount = it.totalPageCount,
                categoryName = it.categoryName
            )
        } ?: BacklogListModel()
    }
}