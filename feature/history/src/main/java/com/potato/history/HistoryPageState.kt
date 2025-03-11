package com.potato.history

import com.poptato.domain.model.response.history.CalendarMonthModel
import com.poptato.domain.model.response.history.HistoryItemModel
import com.poptato.ui.base.PageState
import java.time.LocalDate

data class HistoryPageState(
    val historyList: List<HistoryItemModel> = emptyList(),
    val totalPageCount: Int = -1,
    val isLoadingMore: Boolean = false,
    var currentPage: Int = 0,
    val pageSize: Int = 15,
    val currentMonthStartDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val selectedDate: String = LocalDate.now().toString(),
    val eventDates: List<String> = emptyList(),
    val calendarMonth: CalendarMonthModel = CalendarMonthModel(LocalDate.now().year, LocalDate.now().monthValue),

    ) : PageState