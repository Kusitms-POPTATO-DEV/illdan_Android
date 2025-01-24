package com.poptato.core.util

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Year
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

object TimeFormatter {

    fun getToday(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    fun getTodayFullDate(): String {
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

    fun getTodayMonthDay(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MM.dd")
        return currentDate.format(formatter)
    }

    fun getTodayYearMonthDay(): List<String> {
        val currentDate = LocalDate.now().toString()
        return currentDate.split('-')
    }

    fun getDaysInMonth(year: Int, month: Int): List<Int> {
        val daysInMonth = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (Year.isLeap(year.toLong())) 29 else 28
            else -> 30
        }
        return (1..daysInMonth).toList()
    }

    fun calculateDDay(deadline: String?): Int? {
        if (deadline == null) return null

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now()
        val deadlineDate = LocalDate.parse(deadline, formatter)

        return ChronoUnit.DAYS.between(today, deadlineDate).toInt()
    }
}