package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poptato.core.enums.DateType
import com.poptato.core.util.TimeFormatter
import com.poptato.design_system.Cancel
import com.poptato.design_system.Complete
import com.poptato.design_system.Confirm
import com.poptato.design_system.Day
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray95
import com.poptato.design_system.Month
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.Year
import com.poptato.domain.model.enums.BottomSheetType
import com.poptato.ui.util.fadingEdge

@Composable
fun DatePickerBottomSheet(
    onFullDateSelected: (String) -> Unit = {},
    onSubDateSelected: (Int, Int) -> Unit = {_, _ ->},
    onDismissRequest: () -> Unit = {},
    bottomSheetType: BottomSheetType = BottomSheetType.FullDate
) {
    val todayDate = TimeFormatter.getTodayYearMonthDay()
    val initialYear = todayDate[0].toInt()
    val initialMonth = todayDate[1].toInt()
    val initialDay = todayDate[2].toInt()
    val items = TimeFormatter.getDaysInMonth(initialYear, initialMonth)

    DatePickerBottomSheetContent(
        initialYear = initialYear,
        initialMonth = initialMonth,
        initialDay = initialDay,
        items  = items,
        onDismissRequest = onDismissRequest,
        onFullDateSelected = {
            onFullDateSelected(it)
            onDismissRequest()
        },
        onSubDateSelected = onSubDateSelected,
        bottomSheetType = bottomSheetType
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun DatePickerBottomSheetContent(
    initialYear: Int = -1,
    initialMonth: Int = -1,
    initialDay: Int = -1,
    items: List<Int> = emptyList(),
    onDismissRequest: () -> Unit = {},
    onFullDateSelected: (String) -> Unit = {},
    onSubDateSelected: (Int, Int) -> Unit = {_, _ ->},
    bottomSheetType: BottomSheetType = BottomSheetType.FullDate
) {
    val yearState = rememberLazyListState(initialFirstVisibleItemIndex = initialYear - 2024)
    val monthState = rememberLazyListState(initialFirstVisibleItemIndex = initialMonth - 1)
    val dayState = rememberLazyListState(initialFirstVisibleItemIndex = initialDay - 1)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(324.dp)
            .background(Gray100)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray100)
                .padding(top = 24.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                DialPicker(
                    items = (2024..2100).toList(),
                    listState = yearState,
                    dateType = DateType.YEAR,
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                DialPicker(
                    items = (1..12).toList(),
                    listState = monthState,
                    dateType = DateType.MONTH,
                )
            }

            if (bottomSheetType == BottomSheetType.FullDate) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    DialPicker(
                        items = items,
                        listState = dayState,
                        dateType = DateType.DAY,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                btnColor = Primary60,
                btnText = Confirm,
                textColor = Gray100,
                onClickBtn = {
                    val selectedYear = 2024 + yearState.firstVisibleItemIndex
                    val selectedMonth = 1 + monthState.firstVisibleItemIndex
                    val selectedDay = 1 + dayState.firstVisibleItemIndex
                    val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay)

                    if (bottomSheetType == BottomSheetType.FullDate) {
                        onFullDateSelected(formattedDate)
                    } else if (bottomSheetType == BottomSheetType.SubDate) {
                        onSubDateSelected(selectedYear, selectedMonth)
                    }
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
    dateType: DateType = DateType.YEAR,
) {
    val extendedItems = listOf(0, 0) + items + listOf(0, 0)
    val visibleItemsCount = 5
    val itemHeight = 30.dp
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
                firstVisibleItemIndex + 1, firstVisibleItemIndex + 3 -> PoptatoTypo.lgSemiBold
                firstVisibleItemIndex, firstVisibleItemIndex + 4 -> PoptatoTypo.mdSemiBold
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
                        text = "$item ${dateType.value}",
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

@SuppressLint("ModifierParameter")
@Composable
fun DatePickerButtons(
    btnText: String = "",
    btnColor: Color = Color.Unspecified,
    textColor: Color = Color.Unspecified,
    textStyle: TextStyle = PoptatoTypo.lgSemiBold,
    onClickBtn: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(btnColor)
            .clickable { onClickBtn() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = btnText,
            style = textStyle,
            color = textColor
        )
    }
}