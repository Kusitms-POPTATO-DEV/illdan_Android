package com.poptato.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poptato.design_system.DEADLINE
import com.poptato.design_system.DEADLINE_DDAY
import com.poptato.design_system.Gray50
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray90
import com.poptato.design_system.PoptatoTypo

@Composable
fun DeadlineItem(
    dday: Int? = null
) {

    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(4.dp))
            .background(Gray90)
            .padding(vertical = 2.dp, horizontal = 4.dp),
    ) {
        Text(
            text = if (dday != 0) String.format(DEADLINE, dday) else DEADLINE_DDAY,
            style = PoptatoTypo.xsMedium,
            color = Gray50
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDeadLine() {
    DeadlineItem()
}