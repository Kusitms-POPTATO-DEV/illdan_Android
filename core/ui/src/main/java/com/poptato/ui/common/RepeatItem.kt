package com.poptato.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poptato.design_system.BOOKMARK
import com.poptato.design_system.Gray50
import com.poptato.design_system.Gray90
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary50
import com.poptato.design_system.Primary60
import com.poptato.design_system.R
import com.poptato.design_system.REPEAT

@Composable
fun RepeatItem() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = Gray90, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = "",
                modifier = Modifier.size(12.dp),
                tint = Gray50
            )

            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = REPEAT,
                style = PoptatoTypo.xsSemiBold,
                color = Gray50
            )
        }
    }
}