package com.poptato.ui.event

sealed class BacklogExternalEvent {
    data class UpdateDeadline(val deadline: String?): BacklogExternalEvent()
    data class DeleteTodo(val id: Long): BacklogExternalEvent()
    data class ActiveItem(val id: Long): BacklogExternalEvent()
    data class UpdateBookmark(val id: Long): BacklogExternalEvent()
    data class UpdateCategory(val id: Long): BacklogExternalEvent()
    data class ToggleRepeat(val id: Long): BacklogExternalEvent()
    data class UpdateTime(val info: Pair<Long, String>): BacklogExternalEvent()
}