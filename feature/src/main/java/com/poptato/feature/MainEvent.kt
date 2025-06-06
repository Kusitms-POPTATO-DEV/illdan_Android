package com.poptato.feature

import com.poptato.ui.base.Event

sealed class MainEvent: Event {
    data object ShowTodoBottomSheet: MainEvent()
}