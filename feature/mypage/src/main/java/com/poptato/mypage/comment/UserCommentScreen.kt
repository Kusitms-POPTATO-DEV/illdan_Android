package com.poptato.mypage.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray10
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray20
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray60
import com.poptato.design_system.Gray90
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary40
import com.poptato.mypage.MyPageViewModel
import com.poptato.design_system.R
import com.poptato.design_system.SNACK_BAR_SEND_COMMENT_SUCCESS
import com.poptato.design_system.USER_COMMENT_PLACEHOLDER
import com.poptato.design_system.USER_COMMENT_TITLE
import com.poptato.design_system.USER_CONTACT_GUIDE
import com.poptato.design_system.USER_CONTACT_PLACEHOLDER
import com.poptato.design_system.WORD_CONTACT
import com.poptato.design_system.WORD_CONTENT
import com.poptato.design_system.WORD_SEND
import com.poptato.mypage.MyPageEvent
import com.poptato.mypage.MyPagePageState
import com.poptato.ui.common.PoptatoButton
import com.poptato.ui.common.TopBar

@Composable
fun UserCommentScreen(
    viewModel: MyPageViewModel,
    showSnackBar: (String) -> Unit,
    popScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collect {
            when (it) {
                is MyPageEvent.SendCommentSuccess -> showSnackBar(SNACK_BAR_SEND_COMMENT_SUCCESS)
            }
        }
    }

    UserCommentContent(
        popScreen = popScreen,
        onSendComment = { content, contact ->
            viewModel.sendComment(content, contact)
            popScreen()
        }
    )
}

@Composable
fun UserCommentContent(
    popScreen: () -> Unit,
    onSendComment: (String, String) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        UserCommentTopBar(
            onClick = {
                popScreen()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = WORD_CONTENT,
            style = PoptatoTypo.mdSemiBold,
            color = Gray10
        )

        Spacer(modifier = Modifier.height(8.dp))

        RoundedCardTextField(
            value = content,
            onValueChange = { content = it },
            placeholder = USER_COMMENT_PLACEHOLDER,
            singleLine = false,
            maxLines = 10,
            minHeight = 240.dp,
            maxHeight = 240.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = WORD_CONTACT,
            style = PoptatoTypo.mdSemiBold,
            color = Gray10
        )

        Spacer(modifier = Modifier.height(8.dp))

        RoundedCardTextField(
            value = contact,
            onValueChange = { contact = it },
            placeholder = USER_CONTACT_PLACEHOLDER,
            singleLine = true,
            maxLength = 100
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = USER_CONTACT_GUIDE,
            style = PoptatoTypo.smMedium,
            color = Gray60
        )

        Spacer(modifier = Modifier.weight(1f))

        PoptatoButton(
            buttonText = WORD_SEND,
            textColor = Gray90,
            backgroundColor = Primary40,
            onClickButton = { onSendComment(content, contact) }
        )
    }
}

@Composable
private fun UserCommentTopBar(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = null,
                modifier = Modifier.clickable { onClick() }
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = USER_COMMENT_TITLE,
            style = PoptatoTypo.mdSemiBold,
            color = Gray00
        )
    }
}

@Composable
fun RoundedCardTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = true,
    minHeight: Dp? = null,
    maxHeight: Dp? = null,
    maxLength: Int? = null,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val heightModifier = Modifier.then(
        if (minHeight != null || maxHeight != null) {
            Modifier.heightIn(
                min = minHeight ?: Dp.Unspecified,
                max = maxHeight ?: Dp.Unspecified
            )
        } else Modifier
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(heightModifier)
            .background(
                color = Gray95,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = PoptatoTypo.mdRegular,
                color = Gray40
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { new ->
                if (maxLength == null || new.length <= maxLength) {
                    onValueChange(new)
                }
            },
            singleLine = singleLine,
            maxLines = maxLines,
            textStyle = PoptatoTypo.mdRegular.copy(color = Gray00),
            cursorBrush = SolidColor(Gray00),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = Modifier.fillMaxWidth()
        )
    }
}