package com.poptato.feature.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.poptato.core.enums.BottomNavType
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray80
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.R
import com.poptato.navigation.NavRoutes

@SuppressLint("ModifierParameter")
@Composable
fun BottomNavBar(
    type: BottomNavType = BottomNavType.TODAY,
    onClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Gray100),
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            iconId = if(type == BottomNavType.TODAY) R.drawable.ic_today_selected else R.drawable.ic_today_unselected,
            isSelected = type == BottomNavType.TODAY,
            type = BottomNavType.TODAY,
            onClick = onClick,
            interactionSource = interactionSource
        )
        BottomNavItem(
            iconId = if(type == BottomNavType.BACK_LOG) R.drawable.ic_list_selected else R.drawable.ic_list_unselected,
            isSelected = type == BottomNavType.BACK_LOG,
            type = BottomNavType.BACK_LOG,
            onClick = onClick,
            interactionSource = interactionSource
        )
        BottomNavItem(
            iconId = if(type == BottomNavType.HISTORY) R.drawable.ic_clock_selected else R.drawable.ic_clock_unselected,
            isSelected = type == BottomNavType.HISTORY,
            type = BottomNavType.HISTORY,
            onClick = onClick,
            interactionSource = interactionSource
        )
        BottomNavItem(
            iconId = if(type == BottomNavType.SETTINGS) R.drawable.ic_settings_selected else R.drawable.ic_settings_unselected,
            isSelected = type == BottomNavType.SETTINGS,
            type = BottomNavType.SETTINGS,
            onClick = onClick,
            interactionSource = interactionSource
        )
    }
}

@Composable
fun BottomNavItem(
    iconId: Int = -1,
    isSelected: Boolean = false,
    type: BottomNavType = BottomNavType.DEFAULT,
    onClick: (String) -> Unit = {},
    interactionSource: MutableInteractionSource
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .size(width = 42.dp, height = 46.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    when (type) {
                        BottomNavType.TODAY -> {
                            onClick(NavRoutes.TodayScreen.route)
                        }

                        BottomNavType.BACK_LOG -> {
                            onClick(NavRoutes.BacklogScreen.route)
                        }

                        BottomNavType.HISTORY -> {
                            onClick(NavRoutes.HistoryScreen.route)
                        }

                        BottomNavType.SETTINGS -> {
                            onClick(NavRoutes.MyPageScreen.route)
                        }

                        BottomNavType.DEFAULT -> {}
                    }
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = type.navName,
            style = PoptatoTypo.xsMedium,
            color = if (isSelected) Primary60 else Gray80
        )
    }
}