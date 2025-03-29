package com.poptato.domain.model.response.todo

data class TodoDetailItemModel (
    val isBookmark: Boolean = false,
    val isRepeat: Boolean = false,
    val content: String = "",
    val deadline: String? = "",
    val categoryName: String? = null,
    val emojiImageUrl: String? = null
)