package com.poptato.mypage.policy

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.PolicyTitle
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R

@Composable
fun PolicyViewerScreen(
    goBackToMyPage: () -> Unit = {}
) {

    val viewModel: PolicyViewModel = hiltViewModel()
    val uiState: PolicyPageState by viewModel.uiState.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()

    PolicyViewerContent(
        onClickCloseBtn = { goBackToMyPage() },
        uiState = uiState,
        interactionSource = interactionSource,
        scrollState = scrollState
    )
}

@Composable
fun PolicyViewerContent(
    uiState: PolicyPageState = PolicyPageState(),
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    scrollState: ScrollState = rememberScrollState(),
    onClickCloseBtn: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {
        TitleTopBar(
            onClickCloseBtn = onClickCloseBtn,
            interactionSource = interactionSource
        )

        PolicyData(
            policyContent = uiState.policyModel.content,
            scrollState = scrollState
        )
    }
}

@Composable
fun TitleTopBar(
    onClickCloseBtn: () -> Unit = {},
    interactionSource: MutableInteractionSource
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = PolicyTitle,
            style = PoptatoTypo.mdMedium,
            color = Gray00,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_close_no_bg),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(width = 24.dp, height = 24.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickCloseBtn() }
                )
        )
    }
}

@Composable
fun PolicyData(
    policyContent: String = "",
    scrollState: ScrollState = rememberScrollState()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = policyContent,
            style = PoptatoTypo.smMedium,
            color = Gray40,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPolicy() {
    PolicyViewerContent()
}