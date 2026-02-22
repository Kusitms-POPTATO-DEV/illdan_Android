package com.poptato.domain.model.request.today

data class GetTodayListRequestModel(
    val page: Int = 0,
    val size: Int = 8
)
