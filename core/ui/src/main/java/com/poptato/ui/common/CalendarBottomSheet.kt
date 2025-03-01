package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poptato.core.util.TimeFormatter
import com.poptato.design_system.Complete
import com.poptato.design_system.DELETE
import com.poptato.design_system.FRI
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import com.poptato.design_system.YEAR_MONTH
import com.poptato.design_system.Gray90
import com.poptato.design_system.MON
import com.poptato.design_system.Primary60
import com.poptato.design_system.SAT
import com.poptato.design_system.SUN
import com.poptato.design_system.THU
import com.poptato.design_system.TUE
import com.poptato.design_system.WED
import com.poptato.ui.util.AnalyticsManager
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

@SuppressLint("DefaultLocale")
@Composable
fun CalendarBottomSheet(
    onDateSelected: (String?) -> Unit = {},
    onDismissRequest: () -> Unit = {},
    deadline: String = ""
) {
    val today = LocalDate.now()
    var year by remember { mutableIntStateOf(today.year) }
    var month by remember { mutableIntStateOf(today.monthValue) }
    var days by remember { mutableStateOf(generateCalendarDays(year, month)) }
    var selectedDate by remember { mutableStateOf( if (deadline.isEmpty()) today else LocalDate.parse(deadline) ) }
    val interactionSource = remember { MutableInteractionSource() }

    BackHandler(onBack = { onDismissRequest() })

    CalendarBottomSheetContent(
        year = year,
        month = month,
        selectedDate = selectedDate,
        days = days,
        onClickBtnRight = {
            if (month == 12) {
                year += 1
                month = 1
            } else {
                month += 1
            }
            days = generateCalendarDays(year, month)
        },
        onClickBtnLeft = {
            if (month == 1) {
                year -= 1
                month = 12
            } else {
                month -= 1
            }
            days = generateCalendarDays(year, month)
        },
        onClickBtnComplete = {
            val formattedDate = String.format("%04d-%02d-%02d", selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
            onDateSelected(formattedDate)
        },
        onClickBtnDelete = { onDateSelected(null) },
        onDateSelected = { date -> selectedDate = date },
        onDismissRequest = onDismissRequest,
        interactionSource = interactionSource
    )
}

@Composable
fun CalendarBottomSheetContent(
    year: Int = 0,
    month: Int = 0,
    selectedDate: LocalDate? = null,
    days: List<LocalDate?> = emptyList(),
    onClickBtnRight: () -> Unit = {},
    onClickBtnLeft: () -> Unit = {},
    onClickBtnComplete: () -> Unit = {},
    onClickBtnDelete: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onDismissRequest: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Column(
        modifier = Modifier
            .background(Gray95)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        DateNavigationBar(
            year = year,
            month = month,
            onClickBtnRight = onClickBtnRight,
            onClickBtnLeft = onClickBtnLeft,
            interactionSource = interactionSource
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val weekdays = listOf(SUN, MON, TUE, WED, THU, FRI, SAT)
            items(weekdays) { day ->
                Text(
                    text = day,
                    style = PoptatoTypo.smSemiBold,
                    color = Gray70,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(days) { day ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(
                            enabled = day != null,
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                day?.let { selectedDate ->
                                    onDateSelected(selectedDate)
                                }
                            }
                        )
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (day == selectedDate) Primary60 else Gray95),
                    contentAlignment = Alignment.Center
                ) {
                    day?.let {
                        Text(
                            text = it.dayOfMonth.toString(),
                            style = if (it == selectedDate) PoptatoTypo.smSemiBold else PoptatoTypo.smMedium,
                            color = if (it == selectedDate) Gray100 else Gray00,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp)
        ) {
            DatePickerButtons(
                btnText = DELETE,
                btnColor = Gray90,
                textColor = Gray00,
                textStyle = PoptatoTypo.mdMedium,
                modifier = Modifier.weight(1f),
                onClickBtn = {
                    onClickBtnDelete()
                    onDismissRequest()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            DatePickerButtons(
                btnText = Complete,
                btnColor = Primary60,
                textColor = Gray100,
                textStyle = PoptatoTypo.mdSemiBold,
                modifier = Modifier.weight(1f),
                onClickBtn = {
                    onClickBtnComplete()
                    onDismissRequest()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DateNavigationBar(
    year: Int = 0,
    month: Int = 0,
    onClickBtnRight: () -> Unit,
    onClickBtnLeft: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 46.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left_20),
            contentDescription = null,
            tint = Gray40,
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickBtnLeft() }
                )
        )

        Text(
            text = String.format(YEAR_MONTH, year, month),
            style = PoptatoTypo.mdMedium,
            color = Gray00,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(Alignment.CenterVertically)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_20),
            contentDescription = null,
            tint = Gray40,
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickBtnRight() }
                )
        )
    }
}

fun generateCalendarDays(year: Int, month: Int): List<LocalDate?> {
    val yearMonth = YearMonth.of(year, month)
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val days = mutableListOf<LocalDate?>()
    repeat(startDayOfWeek) { days.add(null) }

    for (day in 1..lastDayOfMonth.dayOfMonth) {
        days.add(yearMonth.atDay(day))
    }

    return days
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCalendarBottomSheet() {
    CalendarBottomSheetContent(
        days = generateCalendarDays(2024, 11)
    )
}