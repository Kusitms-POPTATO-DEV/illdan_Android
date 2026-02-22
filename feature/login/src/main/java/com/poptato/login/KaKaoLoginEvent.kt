package com.poptato.login

import com.poptato.ui.base.Event

sealed class KaKaoLoginEvent: Event {
    data object OnSuccessLogin: KaKaoLoginEvent()
    data object NewUserLogin: KaKaoLoginEvent()
}