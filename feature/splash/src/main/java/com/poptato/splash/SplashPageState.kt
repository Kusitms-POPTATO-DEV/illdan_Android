package com.poptato.splash

import com.poptato.ui.base.PageState

data class SplashPageState(
    val skipLogin: Boolean = false
): PageState
