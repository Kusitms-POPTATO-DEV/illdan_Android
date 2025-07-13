package com.poptato.today

import com.poptato.ui.base.Event

sealed class TodayEvent: Event {
    data object OnFailedUpdateTodayList: TodayEvent()
    data object OnSuccessDeleteTodo: TodayEvent()
    data object TodayAllChecked: TodayEvent()
}