package com.poptato.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.decode.SvgDecoder
import com.poptato.design_system.DEADLINE_MODE_TEXT
import com.poptato.design_system.FAQ
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray20
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray60
import com.poptato.design_system.Gray95
import com.poptato.design_system.Notice
import com.poptato.design_system.Policy
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary40
import com.poptato.design_system.Primary60
import com.poptato.design_system.ProfileDetail
import com.poptato.design_system.R
import com.poptato.design_system.SettingTitle
import com.poptato.design_system.USER_COMMENT_TITLE
import com.poptato.design_system.Version
import com.poptato.design_system.VersionSetting
import com.poptato.mypage.BuildConfig.VERSION_NAME
import com.poptato.mypage.MyPageViewModel.Companion.FAQ_TYPE
import com.poptato.mypage.MyPageViewModel.Companion.NOTICE_TYPE
import com.poptato.ui.common.PoptatoSwitchButton
import com.poptato.ui.util.AnalyticsManager
import timber.log.Timber

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel,
    goToUserDataPage: () -> Unit = {},
    goToPolicyViewerPage: () -> Unit = {},
    goToUserCommentPage: () -> Unit = {}
) {
    val uiState: MyPagePageState by viewModel.uiState.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }

    MyPageContent(
        uiState = uiState,
        onClickSwitchButton = { viewModel.setDeadlineDateMode(it) },
        onClickUserDataBtn = { goToUserDataPage() },
        interactionSource = interactionSource,
        onClickPolicyBtn = { goToPolicyViewerPage() },
        onClickUserComment = { goToUserCommentPage() }
    )
}

@Composable
fun MyPageContent(
    uiState: MyPagePageState = MyPagePageState(),
    onClickSwitchButton: (Boolean) -> Unit = {},
    onClickUserDataBtn: () -> Unit = {},
    onClickPolicyBtn: () -> Unit = {},
    onClickUserComment: () -> Unit = {},
    interactionSource: MutableInteractionSource = MutableInteractionSource()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {

        MyData(
            uiState = uiState,
            onClickUserDataBtn = onClickUserDataBtn
        )

        SettingSubTitle(
            title = SettingTitle,
            topPadding = 24
        )

        DeadlineModeSwitch(
            isDeadlineDateMode = uiState.isDeadlineDateMode,
            onClickSwitchButton = onClickSwitchButton
        )

        SettingServiceItem(
            title = USER_COMMENT_TITLE,
            interactionSource = interactionSource,
            onClickAction = { onClickUserComment() }
        )

        SettingServiceItem(
            title = Policy,
            interactionSource = interactionSource,
            onClickAction = {
                onClickPolicyBtn()
                AnalyticsManager.logEvent(eventName = "terms")
            }
        )

        SettingServiceItem(
            title = Version,
            isVersion = true,
            interactionSource = interactionSource
        )
    }
}

@Composable
fun DeadlineModeSwitch(
    isDeadlineDateMode: Boolean = false,
    onClickSwitchButton: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = DEADLINE_MODE_TEXT,
            color = Gray20,
            style = PoptatoTypo.mdRegular
        )

        Spacer(modifier = Modifier.weight(1f))

        PoptatoSwitchButton(
            check = isDeadlineDateMode,
            onClick = { onClickSwitchButton(!isDeadlineDateMode) }
        )
    }
}

@Composable
fun MyData(
    uiState: MyPagePageState = MyPagePageState(),
    onClickUserDataBtn: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            val imageLoader = ImageLoader.Builder(context)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
            Coil.setImageLoader(imageLoader)

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = uiState.userDataModel.userImg.ifEmpty { R.drawable.ic_person },
                    contentDescription = "img_temp_person",
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        when (state){
                            is AsyncImagePainter.State.Error -> {
                                Timber.d("[이미지] 에러 ${state.result.throwable}")
                            }
                            is AsyncImagePainter.State.Success -> {
                                //
                            }
                            else -> {}
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = uiState.userDataModel.name,
                color = Gray00,
                style = PoptatoTypo.lgMedium,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = null,
                tint = Gray60,
                modifier = Modifier
                    .clickable { onClickUserDataBtn() }
            )
        }
    }
}

@Composable
fun UserDataBtn(
    onClickUserDataBtn: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Gray95)
            .wrapContentHeight()
            .clickable { onClickUserDataBtn() }
    ) {
        Text(
            text = ProfileDetail,
            style = PoptatoTypo.smSemiBold,
            color = Gray00,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 12.dp)
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
            .wrapContentHeight()
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
            style = PoptatoTypo.mdRegular,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (isVersion) {
            Text(
                text = String.format(VersionSetting, VERSION_NAME),
                color = Primary40,
                style = PoptatoTypo.mdRegular,
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
    MyPageContent()
}