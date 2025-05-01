package com.poptato.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.design_system.Gray100
import com.poptato.design_system.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    goToKaKaoLogin: () -> Unit = {},
    goToToday: () -> Unit
) {
    val viewModel: SplashViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        delay(2000L)

        if (uiState.skipLogin) { goToToday() }
        else { goToKaKaoLogin() }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_fire_splash),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSplash() {
    SplashContent()
}