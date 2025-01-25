package com.poptato.app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.sdk.common.KakaoSdk
import com.microsoft.clarity.Clarity
import com.microsoft.clarity.ClarityConfig
import com.poptato.ui.util.AnalyticsManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PoptatoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val config = ClarityConfig(BuildConfig.CLARITY_ID)

        AnalyticsManager.initialize(this)
        Clarity.initialize(this, config)
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY, loggingEnabled = true)
        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
    }
}