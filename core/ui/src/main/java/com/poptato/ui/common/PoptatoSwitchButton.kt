package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray80
import com.poptato.design_system.Primary60
import kotlin.math.roundToInt

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun PoptatoSwitchButton(
    check: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val minBound = with(density) { 0.dp.toPx() }
    val maxBound = with(density) { 20.dp.toPx() }
    val state by animateFloatAsState(
        targetValue = if (check) maxBound else minBound,
        animationSpec = tween(durationMillis = 500),
        label = "poptato_switch"
    )
    Box(
        modifier = modifier
            .size(width = 44.dp, height = 24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (check) Primary60 else Gray80)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(state.roundToInt(), 0) }
                .padding(2.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(Gray00)
        )
    }
}

