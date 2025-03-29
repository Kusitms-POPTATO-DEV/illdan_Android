package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.todo.TodoDetailItemResponse
import com.poptato.domain.model.response.todo.TodoDetailItemModel

object TodoDetailResponseMapper : Mapper<TodoDetailItemResponse, TodoDetailItemModel> {
    override fun responseToModel(response: TodoDetailItemResponse?): TodoDetailItemModel {
        return response?.let {
            TodoDetailItemModel(
                isBookmark = it.isBookmark,
                isRepeat = it.isRepeat,
                content = it.content,
                deadline = it.deadline,
                categoryName = it.categoryName,
                emojiImageUrl = it.emojiImageUrl
            )
        } ?: TodoDetailItemModel()
    }
}