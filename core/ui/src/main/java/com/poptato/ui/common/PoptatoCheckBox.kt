package com.poptato.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.poptato.design_system.R

@Composable
fun PoptatoCheckBox(
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    checkBoxDrawable: Int? = null
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .size(20.dp)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(!isChecked)
            }
            .background(color = Color.Unspecified, shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isChecked) {
            Icon(painter = painterResource(id = checkBoxDrawable ?: R.drawable.ic_checked), contentDescription = "", tint = Color.Unspecified)
        } else {
            Icon(painter = painterResource(id = R.drawable.ic_unchecked), contentDescription = "", tint = Color.Unspecified)
        }
    }
}