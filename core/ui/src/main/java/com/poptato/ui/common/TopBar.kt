package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R

@SuppressLint("ModifierParameter")
@Composable
fun TopBar(
    titleText: String = "",
    subText: String = "",
    titleTextStyle: TextStyle = PoptatoTypo.xxxLSemiBold,
    subTextStyle: TextStyle = PoptatoTypo.mdMedium,
    titleTextColor: Color = Gray00,
    subTextColor: Color = Gray40,
    modifier: Modifier = Modifier,
    isTodayTopBar: Boolean = false,
    isCategorySettingBtn: Boolean = false,
    isCategorySettingBtnSelected: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Gray100)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = titleText,
            style = titleTextStyle,
            color = titleTextColor
        )

        Spacer(modifier = Modifier.width(12.dp))

        if (isTodayTopBar) {
            Icon(
                painter = painterResource(id = R.drawable.ic_today_msg_bubble),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(bottom = 9.dp)
            )
        } else {
            Text(
                text = subText,
                style = subTextStyle,
                color = subTextColor
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isCategorySettingBtn) {
            Icon(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "category setting",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = { isCategorySettingBtnSelected() }
                    )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTopBar() {
    TopBar(
        titleText = "09.28",
        subText = "오늘도 하나씩 해보는 거야!",
        isTodayTopBar = true
    )
}