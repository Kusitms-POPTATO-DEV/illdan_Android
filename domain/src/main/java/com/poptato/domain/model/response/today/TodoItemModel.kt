package com.poptato.domain.model.response.today

import com.poptato.domain.model.enums.TodoStatus

@Suppress("DefaultLocale")
data class TodoItemModel(
    val todoId: Long = -1,
    val content: String = "",
    val todoStatus: TodoStatus = TodoStatus.INCOMPLETE,
    val isBookmark: Boolean = false,
    val deadline: String = "",
    val isRepeat: Boolean = false,
    val dDay: Int? = null,
    var categoryName: String = "",
    var imageUrl: String = "",
    var categoryId: Long = -1,
    var time: String = ""
) {
    private val parsedTime: Triple<String, Int, Int>? = try {
        val parts = time.split(":")
        if (parts.size >= 2) {
            val h24 = parts[0].toInt()
            val minute = parts[1].toInt()
            val meridiem = if (h24 < 12) "오전" else "오후"
            val hour = when {
                h24 == 0 -> 12 // 00:00 -> 12 AM
                h24 in 1..12 -> h24
                else -> h24 - 12
            }
            Triple(meridiem, hour, minute)
        } else null
    } catch (e: Exception) {
        null
    }

    val meridiem: String
        get() = parsedTime?.first ?: ""

    val hour: Int
        get() = parsedTime?.second ?: 0

    val minute: Int
        get() = parsedTime?.third ?: 0

    fun formatTime(value: Triple<String, Int, Int>?): String {
        return if (value == null) "" else {
            var hour = value.second
            val minute = value.third

            if (value.first == "오후") {
                hour += 12
            }

            String.format("%02d:%02d", hour, minute)
        }
    }
}
