package com.poptato.ui.event

sealed class TodoExternalEvent {
    data class UpdateDeadline(val deadline: String?): TodoExternalEvent()
    data class DeleteTodo(val id: Long): TodoExternalEvent()
    data class ActiveItem(val id: Long): TodoExternalEvent()
    data class UpdateBookmark(val id: Long): TodoExternalEvent()
    data class UpdateCategory(val id: Long?): TodoExternalEvent()
    data class UpdateRepeat(val id: Long, val value: Boolean): TodoExternalEvent()
    data class UpdateTime(val info: Pair<Long, String>): TodoExternalEvent()
    data class UpdateRoutine(val id: Long, val days: Set<Int>?): TodoExternalEvent()
}