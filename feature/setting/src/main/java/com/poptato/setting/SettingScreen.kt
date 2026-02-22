package com.poptato.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.poptato.design_system.EditProfile
import com.poptato.design_system.FAQ
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray20
import com.poptato.design_system.Gray60
import com.poptato.design_system.LogOut
import com.poptato.design_system.Notice
import com.poptato.design_system.Policy
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.ProfileTitle
import com.poptato.design_system.R
import com.poptato.design_system.ServiceTitle
import com.poptato.design_system.SettingTitle
import com.poptato.design_system.UserDelete
import com.poptato.design_system.Version
import com.poptato.design_system.VersionSetting
import com.poptato.setting.BuildConfig.VERSION_NAME
import com.poptato.setting.logout.LogOutDialog
import com.poptato.setting.logout.LogOutDialogState

@Composable
fun SettingScreen(
    goBackToMyPage: () -> Unit = {},
    goToEditUserData: () -> Unit = {},
    goToServiceDelete: () -> Unit = {},
    goBackToLogIn: () -> Unit = {}
) {

    val viewModel: SettingViewModel = hiltViewModel()
    val uiState: SettingPageState by viewModel.uiState.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }
    val logOutDialogState = viewModel.logOutDialogState.value

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is SettingEvent.GoBackToLogIn -> {
                    goBackToLogIn()
                }
            }
        }
    }

    SettingContent(
        logOutDialogState = logOutDialogState,
        onClickCloseBtn = { goBackToMyPage() },
        onClickEditUserDataBtn = { goToEditUserData() },
        onClickServiceDeleteBtn = { goToServiceDelete() },
        onClickLogOutBtn = { viewModel.showLogOutDialog() },
        interactionSource = interactionSource
    )
}

@Composable
fun SettingContent(
    logOutDialogState: LogOutDialogState,
    onClickCloseBtn: () -> Unit = {},
    onClickEditUserDataBtn: () -> Unit = {},
    onClickServiceDeleteBtn: () -> Unit = {},
    onClickLogOutBtn: () -> Unit = {},
    interactionSource: MutableInteractionSource = MutableInteractionSource()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {

        SettingTitle(
            onClickCloseBtn = onClickCloseBtn
        )

        SettingSubTitle(
            title = ProfileTitle
        )
        SettingServiceItem(
            title = EditProfile,
            onClickAction = onClickEditUserDataBtn,
            interactionSource = interactionSource
        )
        SettingServiceItem(
            title = LogOut,
            onClickAction = onClickLogOutBtn,
            interactionSource = interactionSource
        )

        SettingSubTitle(
            title = ServiceTitle,
            topPadding = 40
        )
        SettingServiceItem(
            title = Notice,
            interactionSource = interactionSource
        )
        SettingServiceItem(
            title = FAQ,
            interactionSource = interactionSource
        )
        SettingServiceItem(
            title = Policy,
            interactionSource = interactionSource
        )
        SettingServiceItem(
            title = Version,
            isVersion = true,
            interactionSource = interactionSource
        )
        SettingServiceItem(
            title = UserDelete,
            color = Gray60,
            onClickAction = { onClickServiceDeleteBtn() },
            interactionSource = interactionSource
        )
    }

    if (logOutDialogState.isShowDialog) {
        LogOutDialog(
            onDismiss = logOutDialogState.onDismissRequest,
            onClickBack = logOutDialogState.onClickBackBtn,
            onClickLogOut = logOutDialogState.onClickLogOutBtn
        )
    }
}

@Composable
fun SettingTitle(
    onClickCloseBtn: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = SettingTitle,
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
                .clickable { onClickCloseBtn() }
        )
    }
}

@Composable
fun SettingSubTitle(
    title: String,
    topPadding: Int = 0
) {
    Text(
        text = title,
        color = Gray00,
        style = PoptatoTypo.lgSemiBold,
        modifier = Modifier
            .padding(start = 16.dp, top = topPadding.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingServiceItem(
    title: String,
    color: Color = Gray20,
    isVersion: Boolean = false,
    onClickAction: () -> Unit = {},
    interactionSource: MutableInteractionSource
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(start = 24.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { onClickAction() }
            )
    ) {
        Text(
            text = title,
            color = color,
            style = PoptatoTypo.mdMedium
        )

        if (isVersion) {
            Text(
                text = String.format(VersionSetting, VERSION_NAME),
                color = Primary60,
                style = PoptatoTypo.mdMedium,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSetting() {
    SettingContent(logOutDialogState = LogOutDialogState())
}