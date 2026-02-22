package com.poptato.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.START
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(
    goToBacklog: () -> Unit
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { uiState.onboardingList.size })

    OnboardingContent(
        onboardingList = uiState.onboardingList,
        pagerState = pagerState,
        goToBacklog = goToBacklog
    )
}

@Composable
fun OnboardingContent(
    onboardingList: List<OnboardingModel> = emptyList(),
    pagerState: PagerState,
    goToBacklog: () -> Unit
) {
    val currentPage = pagerState.currentPage
    val composition by rememberLottieComposition(onboardingList[pagerState.currentPage].resource)
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(currentPage) {
        progress = 0f
        while (true) {
            progress += 0.0035f
            delay(16L)
            if (progress > 1f) progress = 0f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = onboardingList[pagerState.currentPage].title,
                style = PoptatoTypo.xxLSemiBold,
                color = Gray00,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1f),
                key = { index -> onboardingList[index].id }
            ) {
                if (it == currentPage) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Gray100)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            PageIndicator(
                numberOfPages = pagerState.pageCount,
                selectedPage = pagerState.currentPage
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = if (pagerState.currentPage == 3) {
                            Primary60
                        } else {
                            Gray95
                        }
                    )
                    .clickable { goToBacklog() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = START,
                    style = PoptatoTypo.mdSemiBold,
                    color = if (pagerState.currentPage == 3) {
                        Gray100
                    } else {
                        Gray70
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}