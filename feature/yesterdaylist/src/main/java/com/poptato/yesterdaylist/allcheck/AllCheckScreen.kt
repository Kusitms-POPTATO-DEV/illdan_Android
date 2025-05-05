package com.poptato.yesterdaylist.allcheck

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary10
import com.poptato.design_system.R
import com.poptato.design_system.YesterdayAllCheckContent
import kotlinx.coroutines.delay

@Composable
fun AllCheckScreen(
    goBackToBacklog: () -> Unit = {}
) {

    LaunchedEffect(Unit) {
        delay(2000L)
        goBackToBacklog()
    }

    AllCheckContent()
}

@Composable
fun AllCheckContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "",
                style = PoptatoTypo.xLSemiBold,
                color = Gray00
            )

            Spacer(modifier = Modifier.height(52.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_fire_yesterday),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAllCheck() {
    AllCheckContent()
}