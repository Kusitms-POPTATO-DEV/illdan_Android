package com.poptato.setting.userdata

import com.poptato.ui.base.Event

sealed class UserDataEvent : Event {
    data object GoBackToLogIn : UserDataEvent()
}