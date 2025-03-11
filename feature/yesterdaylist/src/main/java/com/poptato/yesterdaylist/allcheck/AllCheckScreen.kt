package com.poptato.yesterdaylist.allcheck

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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

        Image(
            painter = painterResource(id = R.drawable.ic_all_check_bg),
            contentDescription = "ic_all_check_bg",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-490).dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_star4),
                contentDescription = "ic_star4",
                modifier = Modifier
                    .offset(x = 24.dp, y = (-24).dp)
                    .size(16.dp)
            )

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_star2),
                    contentDescription = "ic_star2",
                    modifier = Modifier
                        .offset(y = (-8).dp)
                        .size(24.dp))

                Text(
                    text = YesterdayAllCheckContent,
                    style = PoptatoTypo.xxLMedium,
                    color = Primary10,
                    textAlign = TextAlign.Center)


                Image(
                    painter = painterResource(id = R.drawable.ic_star3),
                    contentDescription = "ic_star3",
                    modifier = Modifier
                        .offset(x = 4.dp, y = 50.dp)
                        .size(32.dp))
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_all_check_bg_bottom),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.Crop
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAllCheck() {
    AllCheckContent()
}