package com.poptato.setting.servicedelete

import com.poptato.ui.base.Event

sealed class ServiceDeleteEvent : Event {
    data object GoBackToLogIn : ServiceDeleteEvent()
}