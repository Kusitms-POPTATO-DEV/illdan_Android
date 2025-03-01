package com.poptato.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poptato.core.enums.DateType
import com.poptato.core.util.TimeFormatter
import com.poptato.design_system.Cancel
import com.poptato.design_system.Confirm
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray30
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray95
import com.poptato.design_system.Month
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.YEAR_MONTH
import com.poptato.design_system.Year
import com.poptato.domain.model.response.history.CalendarMonthModel
import com.poptato.ui.R
import com.poptato.ui.util.fadingEdge
import timber.log.Timber
import java.time.LocalDate

@Composable
fun MonthPickerBottomSheet(
    initialMonthModel: CalendarMonthModel,
    onYearMonthSelected: (CalendarMonthModel) -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    val currentYear = LocalDate.now().year
    val yearState = rememberLazyListState(initialFirstVisibleItemIndex = initialMonthModel.year - 2000)
    val monthState = rememberLazyListState(initialFirstVisibleItemIndex = initialMonthModel.month - 1)

    val selectedYear = remember { derivedStateOf { 2000 + yearState.firstVisibleItemIndex } }
    val selectedMonth = remember { derivedStateOf { 1 + monthState.firstVisibleItemIndex } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(324.dp)
            .background(Gray95)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format(YEAR_MONTH, selectedYear.value, selectedMonth.value),
                style = PoptatoTypo.mdMedium,
                color = Gray00
            )
            Spacer(modifier = Modifier.width(7.dp))
            Icon(
                painter = painterResource(id = com.poptato.design_system.R.drawable.ic_triangle_up),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp),
                tint = Gray40
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 41.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.width(85.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.weight(2f)
                ) {
                    DialPicker(
                        items = (2000..currentYear).toList(),
                        listState = yearState
                    )
                }
                Text(
                    text = Year,
                    style = PoptatoTypo.xLMedium,
                    modifier = Modifier.padding(start = 4.dp),
                    color = Gray30
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier.width(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.weight(2f)
                ) {
                    DialPicker(
                        items = (1..12).toList(),
                        listState = monthState,
                    )
                }
                Text(
                    text = Month,
                    style = PoptatoTypo.xLMedium,
                    modifier = Modifier.padding(start = 4.dp),
                    color = Gray30
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DatePickerButtons(
                btnColor = Gray95,
                btnText = Cancel,
                textColor = Gray40,
                onClickBtn = onDismissRequest,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            DatePickerButtons(
                btnColor = Color.Transparent,
                btnText = Confirm,
                textColor = Gray40,
                onClickBtn = {
                    val selectedModel = CalendarMonthModel(selectedYear.value, selectedMonth.value)
                    onYearMonthSelected(selectedModel)
                    onDismissRequest()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DialPicker(
    items: List<Int>,
    listState: LazyListState,
) {
    val extendedItems = listOf(0, 0) + items + listOf(0, 0)
    val visibleItemsCount = 5
    val itemHeight = 40.dp
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .height(itemHeight * visibleItemsCount)
            .fadingEdge(fadingEdgeGradient),
        flingBehavior = flingBehavior
    ) {
        items(extendedItems.size) { index ->
            val item = extendedItems[index]
            val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
            val fontStyle = when (index) {
                firstVisibleItemIndex + 2 -> PoptatoTypo.xLSemiBold
                firstVisibleItemIndex + 1, firstVisibleItemIndex + 3 -> PoptatoTypo.lgMedium
                firstVisibleItemIndex, firstVisibleItemIndex + 4 -> PoptatoTypo.mdMedium
                else -> PoptatoTypo.smSemiBold
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                if (item != 0) {
                    Text(
                        text = "$item",
                        style = fontStyle,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Gray00
                    )
                }
            }
        }
    }
}
