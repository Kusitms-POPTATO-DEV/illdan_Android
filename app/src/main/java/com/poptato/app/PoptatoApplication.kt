package com.poptato.app

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.sdk.common.KakaoSdk
import com.microsoft.clarity.Clarity
import com.microsoft.clarity.ClarityConfig
import com.poptato.ui.util.AnalyticsManager
import com.poptato.di.AppLifecycleObserver
import com.poptato.di.AppLifecycleObserverEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PoptatoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
        )

        val entryPoint = EntryPointAccessors.fromApplication(
            this,
            AppLifecycleObserverEntryPoint::class.java
        )
        val observer = AppLifecycleObserver(entryPoint.tokenRefreshHandler())

        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)

        initClarityAndSdk()
    }

    private fun initClarityAndSdk() {
        val config = ClarityConfig(BuildConfig.CLARITY_ID)

        AnalyticsManager.initialize(this)
        Clarity.initialize(this, config)
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY, loggingEnabled = true)
        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
    }
}