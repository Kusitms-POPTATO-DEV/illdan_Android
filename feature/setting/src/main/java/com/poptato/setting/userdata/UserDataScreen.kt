package com.poptato.setting.userdata

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.design_system.Danger40
import com.poptato.design_system.Danger50
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray95
import com.poptato.design_system.LogOut
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.ProfileDetail
import com.poptato.design_system.R

@Composable
fun EditUserDataScreen(
    goBackToSetting: () -> Unit = {},
) {

    val viewModel: UserDataViewModel = hiltViewModel()
    val uiState: UserDataPageState by viewModel.uiState.collectAsStateWithLifecycle()

    EditUserDataContent(
        uiState = uiState,
        onClickCloseBtn = { goBackToSetting() },
        onValueChange = { newValue -> viewModel.onValueChange(newValue) }
    )
}

@Composable
fun EditUserDataContent(
    uiState: UserDataPageState = UserDataPageState(),
    onClickCloseBtn: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {

        SettingTitle(
            onClickCloseBtn = onClickCloseBtn
        )

        MyData(
            uiState = uiState
        )

        LogOutBtn()
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

        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(width = 24.dp, height = 24.dp)
                .clickable { onClickCloseBtn() }
        )

        Text(
            text = ProfileDetail,
            style = PoptatoTypo.mdMedium,
            color = Gray00,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun MyData(
    uiState: UserDataPageState = UserDataPageState()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "img_temp_person",
                modifier = Modifier.size(64.dp)
            )

            Column {
                Text(
                    text = "이름",
                    color = Gray00,
                    style = PoptatoTypo.lgSemiBold,
                    modifier = Modifier
                        .offset(x = 12.dp, y = 8.dp)
                )

                Text(
                    text = "메일",
                    color = Gray40,
                    style = PoptatoTypo.smRegular,
                    modifier = Modifier
                        .offset(x = 12.dp, y = 10.dp)
                )
            }
        }
    }
}

@Composable
fun LogOutBtn() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Danger50.copy(alpha = 0.1f))
            .wrapContentHeight()
    ) {
        Text(
            text = LogOut,
            style = PoptatoTypo.smSemiBold,
            color = Danger40,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 11.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSetting() {
    EditUserDataContent()
}