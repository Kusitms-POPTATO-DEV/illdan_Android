package com.poptato.ui.common

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
import com.poptato.design_system.Gray20
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.domain.model.response.dialog.DialogContentModel

@Composable
fun TwoBtnTypeDialog(
    onDismiss: () -> Unit = {},
    dialogContent: DialogContentModel = DialogContentModel()
) {
    val interactionSource = remember { MutableInteractionSource() }

    TwoBtnTypeDialogContent(
        onDismiss = onDismiss,
        dialogContent = dialogContent,
        interactionSource = interactionSource
    )
}

@Composable
fun TwoBtnTypeDialogContent(
    onDismiss: () -> Unit = {},
    dialogContent: DialogContentModel = DialogContentModel(),
    interactionSource: MutableInteractionSource = MutableInteractionSource()
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .width(328.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Gray100
        ) {
            Column {

                if (dialogContent.dialogContentText.isNotEmpty()) {
                    Text(
                        text = dialogContent.titleText,
                        textAlign = TextAlign.Center,
                        color = Gray00,
                        style = PoptatoTypo.mdSemiBold,
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = dialogContent.dialogContentText,
                        textAlign = TextAlign.Center,
                        color = Gray20,
                        style = PoptatoTypo.mdMedium,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 32.dp)
                            .align(Alignment.CenterHorizontally))
                } else {
                    Text(
                        text = dialogContent.titleText,
                        textAlign = TextAlign.Center,
                        color = Gray00,
                        style = PoptatoTypo.mdSemiBold,
                        modifier = Modifier
                            .padding(vertical = 40.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                TwoBtnTypeDialogBtn(
                    interactionSource = interactionSource,
                    dialogContent = dialogContent,
                    onDismiss = onDismiss)
            }
        }
    }
}

@Composable
fun TwoBtnTypeDialogBtn(
    dialogContent: DialogContentModel = DialogContentModel(),
    onDismiss: () -> Unit = {},
    interactionSource: MutableInteractionSource
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = dialogContent.cancelBtnText,
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
                    onClick = { onDismiss() }
                )
        )

        Text(
            text = dialogContent.positiveBtnText,
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
                    onClick = {
                        dialogContent.positiveBtnAction()
                        onDismiss()
                    }
                )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTwoBtnDialog() {
    TwoBtnTypeDialogContent()
}