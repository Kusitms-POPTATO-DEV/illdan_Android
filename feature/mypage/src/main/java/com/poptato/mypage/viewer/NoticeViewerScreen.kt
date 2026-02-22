package com.poptato.mypage.viewer

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.poptato.mypage.BuildConfig

@Composable
fun NoticeViewerScreen(
    goBackToMyPage: () -> Unit = {}
) {

    val noticeWebView = rememberWebViewState(
        url = BuildConfig.NOTICE_URL,
        additionalHttpHeaders = emptyMap()
    )
    val webviewClient = AccompanistWebViewClient()
    val webChromeClient = AccompanistWebChromeClient()
    val webViewNavigator = rememberWebViewNavigator()

    CreateNoticeWebViewer(
        webViewState = noticeWebView,
        webViewClient = webviewClient,
        webChromeClient = webChromeClient,
        webViewNavigator = webViewNavigator,
        onClickBackBtn = { goBackToMyPage() }
    )
}

@Composable
fun CreateNoticeWebViewer(
    webViewState: WebViewState,
    webViewClient: AccompanistWebViewClient,
    webChromeClient: AccompanistWebChromeClient,
    webViewNavigator: WebViewNavigator,
    onClickBackBtn: () -> Unit = {}
) {

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        client = webViewClient,
        chromeClient = webChromeClient,
        onCreated = { webView ->
            with (webView) {
                settings.run {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    javaScriptCanOpenWindowsAutomatically = false
                }
            }
        }
    )

    BackHandler(enabled = true) {
        if (webViewNavigator.canGoBack) {
            webViewNavigator.navigateBack()
        } else {
            onClickBackBtn()
        }
    }
}