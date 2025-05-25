package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.poptato.design_system.Gray90
import com.poptato.design_system.PoptatoTypo

@SuppressLint("ModifierParameter")
@Composable
fun PoptatoButton(
    buttonText: String,
    textColor: Color = Gray90,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClickButton: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable {
                if (isEnabled) {
                    onClickButton()
                }
            }
            .background(color = backgroundColor)
            .padding(vertical = 14.5.dp)
            .indication(interactionSource, LocalIndication.current),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonText,
            style = PoptatoTypo.lgSemiBold,
            color = textColor
        )
    }
}