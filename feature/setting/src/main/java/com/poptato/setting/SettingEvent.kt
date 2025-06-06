package com.poptato.setting

import com.poptato.ui.base.Event

sealed class SettingEvent : Event {
    data object GoBackToLogIn: SettingEvent()
}