package com.poptato.domain.model.response.history

import java.time.LocalDate
import java.time.YearMonth


data class CalendarMonthModel(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue
)