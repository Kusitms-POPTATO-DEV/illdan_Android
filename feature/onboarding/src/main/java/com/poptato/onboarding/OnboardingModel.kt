package com.poptato.onboarding

import com.airbnb.lottie.compose.LottieCompositionSpec

data class OnboardingModel(
    val id: Long = -1,
    val title: String = "",
    val resource: LottieCompositionSpec
)
