package com.poptato.onboarding

import com.poptato.ui.base.PageState

data class OnboardingPageState(
    val onboardingList: List<OnboardingModel> = emptyList()
): PageState