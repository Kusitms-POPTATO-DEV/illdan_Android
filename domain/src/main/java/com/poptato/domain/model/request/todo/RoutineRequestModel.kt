package com.poptato.domain.model.request.todo

data class RoutineRequestModel(
    var routineDays: List<String>? = null
) {
    fun convertIndexToDays(index: List<Int>?) {
        if (index == null) { routineDays = null }
        else {
            val weekdays = listOf("월", "화", "수", "목", "금", "토", "일")
            routineDays = index.map { weekdays[it] }
        }
    }
}
