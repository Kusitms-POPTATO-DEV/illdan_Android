package com.poptato.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(
    private val tokenRefreshHandler: TokenRefreshHandler
) : DefaultLifecycleObserver {

    private var isFirstLaunch = true

    override fun onStart(owner: LifecycleOwner) {
        if (isFirstLaunch) {
            isFirstLaunch = false
        } else {
            tokenRefreshHandler.reissueToken()
        }
    }
}