package com.poptato.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.poptato.design_system.BgSnackBar
import com.poptato.design_system.Gray00
import com.poptato.design_system.PoptatoTypo

@Composable
fun CommonSnackBar(
    hostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { snackBarData ->
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(8.dp)),
                backgroundColor = BgSnackBar,
                contentColor = Gray00,
            ) {
                Text(
                    text = snackBarData.visuals.message,
                    style = PoptatoTypo.smMedium,
                    color = Gray00
                )
            }
        }
    )
}