package com.poptato.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.user.UserApiClient
import com.poptato.design_system.BtnKaKaoLoginText
import com.poptato.design_system.Gray100
import com.poptato.design_system.KaKaoLogin
import com.poptato.design_system.KaKaoMain
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import com.poptato.design_system.SUCCESS_LOGIN
import com.poptato.ui.util.FCMManager
import com.poptato.ui.util.LoadingManager
import com.poptato.ui.viewModel.GuideViewModel
import timber.log.Timber

@Composable
fun KaKaoLoginScreen(
    goToToday: () -> Unit = {},
    showSnackBar: (String) -> Unit,
    goToBacklog: () -> Unit = {}
) {
    val viewModel: KaKaoLoginViewModel = hiltViewModel()
    val guideViewModel: GuideViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        LoadingManager.endLoading()
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is KaKaoLoginEvent.OnSuccessLogin -> {
                    goToToday()
                    showSnackBar(SUCCESS_LOGIN)
                }
                is KaKaoLoginEvent.NewUserLogin -> {
                    guideViewModel.updateIsNewUser(true)
                    showSnackBar(SUCCESS_LOGIN)
                    goToBacklog()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        FCMManager.getFCMToken { token ->
            if (token != null) { viewModel.getClientId(token) }
        }
    }

    KaKaoLoginContent(
        onSuccessKaKaoLogin = { viewModel.kakaoLogin(it) },
        context = context
    )
}

@Composable
fun KaKaoLoginContent(
    onSuccessKaKaoLogin: (String) -> Unit = {},
    context: Context
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_login),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .background(KaKaoMain, shape = RoundedCornerShape(8.dp))
                    .clickable { signInKakao(context, onSuccessKaKaoLogin) },
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_kakao),
                        contentDescription = "ic_kakao"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = BtnKaKaoLoginText,
                        style = PoptatoTypo.mdMedium,
                        color = Gray100
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun signInKakao(context: Context, onSuccessKaKaoLogin: (String) -> Unit) {
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        signInKakaoApp(context, onSuccessKaKaoLogin)
    } else {
        signInKakaoEmail(context, onSuccessKaKaoLogin)
    }
}

private fun signInKakaoApp(context: Context, onSuccessKaKaoLogin: (String) -> Unit) {
    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
        if (error != null) {
            Timber.tag("KaKao Login Error").e(error.stackTraceToString())
            signInKakaoEmail(context, onSuccessKaKaoLogin)
            return@loginWithKakaoTalk
        }
        token?.let {
            onSuccessKaKaoLogin(token.accessToken)
        }
    }
}

private fun signInKakaoEmail(context: Context, onSuccessKaKaoLogin: (String) -> Unit) {
    UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
        if (error != null) {
            Timber.tag("KaKao Login Error").e(error.stackTraceToString())
            return@loginWithKakaoAccount
        }
        token?.let {
            onSuccessKaKaoLogin(token.accessToken)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewKaKaoLogin() {
    KaKaoLoginContent(context = LocalContext.current)
}