package com.poptato.setting.logout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.poptato.design_system.Danger50
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray05
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray95
import com.poptato.design_system.LogOutDialogBackBtn
import com.poptato.design_system.LogOutDialogDoBtn
import com.poptato.design_system.LogOutDialogTitle
import com.poptato.design_system.PoptatoTypo

@Composable
fun LogOutDialog(
    onDismiss: () -> Unit = {},
    onClickLogOut: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .width(328.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Gray100
        ) {
            Column {
                Text(
                    text = LogOutDialogTitle,
                    textAlign = TextAlign.Center,
                    color = Gray00,
                    style = PoptatoTypo.mdSemiBold,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .align(Alignment.CenterHorizontally)
                )

                LogOutDialogBtnContent(
                    onClickBack = onClickBack,
                    onClickLogOut = onClickLogOut,
                    interactionSource = interactionSource
                )
            }
        }
    }
}

@Composable
fun LogOutDialogBtnContent(
    onClickBack: () -> Unit = {},
    onClickLogOut: () -> Unit = {},
    interactionSource: MutableInteractionSource
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = LogOutDialogBackBtn,
            textAlign = TextAlign.Center,
            color = Gray05,
            style = PoptatoTypo.mdSemiBold,
            modifier = Modifier
                .background(color = Gray95)
                .padding(vertical = 16.dp)
                .weight(1f)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickBack() }
                )
        )

        Text(
            text = LogOutDialogDoBtn,
            textAlign = TextAlign.Center,
            color = Gray100,
            style = PoptatoTypo.mdSemiBold,
            modifier = Modifier
                .background(color = Danger50)
                .padding(vertical = 16.dp)
                .weight(1f)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickLogOut() }
                )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSetting() {
    LogOutDialog()
}