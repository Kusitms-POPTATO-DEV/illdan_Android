package com.poptato.onboarding

import com.airbnb.lottie.compose.LottieCompositionSpec
import com.poptato.design_system.ONBOARDING1
import com.poptato.design_system.ONBOARDING2
import com.poptato.design_system.ONBOARDING3
import com.poptato.design_system.ONBOARDING4
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(

) : BaseViewModel<OnboardingPageState>(OnboardingPageState()) {
    init {
        updateState(
            uiState.value.copy(
                onboardingList = listOf(
                    OnboardingModel(
                        id = 1,
                        title = ONBOARDING1,
                        resource = LottieCompositionSpec.RawRes(com.poptato.design_system.R.raw.onboarding1)
                    ),
                    OnboardingModel(
                        id = 2,
                        title = ONBOARDING2,
                        resource = LottieCompositionSpec.RawRes(com.poptato.design_system.R.raw.onboarding2)
                    ),
                    OnboardingModel(
                        id = 3,
                        title = ONBOARDING3,
                        resource = LottieCompositionSpec.RawRes(com.poptato.design_system.R.raw.onboarding3)
                    ),OnboardingModel(
                        id = 4,
                        title = ONBOARDING4,
                        resource = LottieCompositionSpec.RawRes(com.poptato.design_system.R.raw.onboarding4)
                    )
                )
            )
        )
    }
}