package com.poptato.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.poptato.design_system.Gray90
import com.poptato.design_system.PoptatoTypo

@Composable
fun PoptatoButton(
    buttonText: String,
    backgroundColor: Color,
    onClickButton: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = backgroundColor)
            .padding(vertical = 14.5.dp)
            .clickable { onClickButton() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonText,
            style = PoptatoTypo.lgSemiBold,
            color = Gray90
        )
    }
}