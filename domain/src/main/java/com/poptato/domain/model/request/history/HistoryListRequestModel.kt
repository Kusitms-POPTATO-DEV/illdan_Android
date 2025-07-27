package com.poptato.domain.model.request.history

import java.time.LocalDate

data class HistoryListRequestModel(
    val page: Int,
    val size: Int,
    val date: String
)
