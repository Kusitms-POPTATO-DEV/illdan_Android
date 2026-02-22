package com.poptato.data.mapper

import com.poptato.data.base.Mapper
import com.poptato.data.model.response.history.HistoryCalendarListResponse
import com.poptato.domain.model.response.history.HistoryCalendarListModel
import com.poptato.domain.model.response.history.HistoryCalendarModel

object HistoryCalendarResponseMapper : Mapper<HistoryCalendarListResponse, HistoryCalendarListModel> {
    override fun responseToModel(response: HistoryCalendarListResponse?): HistoryCalendarListModel {
        return response?.let {
            HistoryCalendarListModel(
                dates = it.dates.map { item ->
                    HistoryCalendarModel(
                        date = item.date,
                        count = item.count
                    )
                }
            )
        } ?: HistoryCalendarListModel()
    }
}