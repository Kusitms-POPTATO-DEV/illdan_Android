package com.poptato.feature

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.microsoft.clarity.Clarity
import com.poptato.design_system.BgProgressBar
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray80
import com.poptato.design_system.Primary60
import com.poptato.ui.util.LoadingManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = window.decorView.rootView
        Clarity.maskView(rootView)

        enableEdgeToEdge()
        setContent {
            val statusBarColor = Gray100.toArgb()
            val isLightIcons = false

            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = statusBarColor
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightIcons

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                MainScreen()
            }
        }
    }
}