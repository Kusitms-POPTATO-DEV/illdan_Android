package com.potato.history

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray80
import com.poptato.design_system.Gray95
import com.poptato.design_system.HistoryListEmpty
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import com.poptato.domain.model.response.history.HistoryItemModel
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.poptato.core.util.reachedLastItem
import com.poptato.design_system.FRI
import com.poptato.design_system.Gray40
import com.poptato.design_system.MON
import com.poptato.design_system.SAT
import com.poptato.design_system.SUN
import com.poptato.design_system.THU
import com.poptato.design_system.TUE
import com.poptato.design_system.WED
import com.poptato.domain.model.enums.MonthNav
import com.poptato.domain.model.response.history.CalendarMonthModel
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    showBottomSheet: (CalendarMonthModel) -> Unit,
    updateMonthFlow: SharedFlow<CalendarMonthModel>
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val uiState: HistoryPageState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(updateMonthFlow) {
        updateMonthFlow.collect { selectedMonth ->
            viewModel.updateCurrentMonth(LocalDate.of(selectedMonth.year, selectedMonth.month, 1))
            viewModel.getCalendarList()
        }
    }

    HistoryContent(
        uiState = uiState,
        onLoadNextPage = { viewModel.getHistoryList() },
        onDateSelected = { selectedDate -> viewModel.updateSelectedDate(selectedDate)},
        onPreviousMonthClick = {viewModel.onClickCalNav(MonthNav.PREVIOUS)},
        onNextMonthClick = {viewModel.onClickCalNav(MonthNav.NEXT)},
        getImageResourceForDate = { date, hasEvent -> viewModel.getImageResourceForDate(date, hasEvent) },
        onClickCalendarHeader = {
            viewModel.setCalendarMonthModel { updatedCalendarMonth ->
                showBottomSheet(updatedCalendarMonth)
            }
        }
    )
}

@Composable
fun HistoryContent(
    uiState: HistoryPageState = HistoryPageState(),
    onLoadNextPage: () -> Unit,
    onDateSelected: (String) -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    getImageResourceForDate: (LocalDate, Boolean) -> Int,
    onClickCalendarHeader: () -> Unit
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
            .padding(start = 14.dp, end = 14.dp)
    ) {

        CalendarContent(
            currentMonthStartDate = uiState.currentMonthStartDate,
            selectedDate = uiState.selectedDate,
            onDateSelected = onDateSelected,
            eventDates = uiState.eventDates,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            getImageResourceForDate = getImageResourceForDate,
            onClickCalendarHeader = onClickCalendarHeader
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (uiState.historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = HistoryListEmpty,
                    color = Gray80,
                    style = PoptatoTypo.lgMedium
                )
            }
        } else {
            InfinityLazyColumn(
                state = listState,
                loadMore = onLoadNextPage
            ) {
                items(uiState.historyList) { item ->
                    HistoryListItem(item = item)
                }
            }

            LaunchedEffect(Unit) {
                snapshotFlow { uiState.historyList }
                    .collect {
                        onLoadNextPage()
                    }
            }
        }
    }
}

@Composable
fun CalendarContent(
    currentMonthStartDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    selectedDate: String = LocalDate.now().toString(),
    onDateSelected: (String) -> Unit = {},
    eventDates: List<String>,
    onNextMonthClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    getImageResourceForDate: (LocalDate, Boolean) -> Int,
    onClickCalendarHeader: () -> Unit,
) {
    val daysInMonth = currentMonthStartDate.lengthOfMonth()
    val firstDayOfWeek = (currentMonthStartDate.withDayOfMonth(1).dayOfWeek.value % 7) // 일요일 = 0
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray100)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        CalendarHeader(
            currentMonth = currentMonthStartDate,
            onPreviousMonthClick = { onPreviousMonthClick() },
            onNextMonthClick = { onNextMonthClick() },
            onHeaderClick = { onClickCalendarHeader() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        DayOfWeekHeader()

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(36.dp))
            }
            
            items((1..daysInMonth).toList()) { day ->
                val date = currentMonthStartDate.withDayOfMonth(day)
                CalendarDayItem(
                    day = day,
                    date = date,
                    isSelected = selectedDate == date.toString(),
                    isToday = today == date,
                    hasEvent = eventDates.contains(date.toString()),
                    onClick = { onDateSelected(date.toString()) },
                    imageResource = getImageResourceForDate(date, eventDates.contains(date.toString()))
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: LocalDate,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onHeaderClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonthClick, modifier = Modifier.size(20.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left_20),
                contentDescription = "Previous Month",
                tint = Gray40
            )
        }
        Row(
            modifier = Modifier
                .clickable { onHeaderClick() }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월")),
                style = PoptatoTypo.mdMedium,
                color = Gray00,
            )
            Spacer(modifier = Modifier.width(7.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_triangle_down),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp),
                tint = Gray40
            )
        }
        IconButton(onClick = onNextMonthClick, modifier = Modifier.size(20.dp), enabled = currentMonth < LocalDate.now().withDayOfMonth(1)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_20),
                contentDescription = "Next Month",
                tint = when{
                    currentMonth >= LocalDate.now().withDayOfMonth(1) -> Gray80
                    else -> Gray40
                }
            )
        }
    }
}

@Composable
fun DayOfWeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf(SUN, MON, TUE, WED, THU, FRI, SAT).forEach { day ->
            Text(
                text = day,
                style = PoptatoTypo.smMedium,
                fontSize = 11.sp,
                color = Gray80,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CalendarDayItem(
    day: Int,
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasEvent: Boolean,
    onClick: () -> Unit,
    imageResource: Int
) {
    Column(
        modifier = Modifier
            .size(36.dp, 56.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )

        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = when {
                        isSelected -> Gray00
                        else -> Color.Transparent
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.toString(),
                style = PoptatoTypo.smMedium,
                fontSize = 10.sp,
                color = when {
                    isSelected -> Gray95
                    else -> Gray70
                }
            )
        }
    }
}

@Composable
fun HistoryListItem(item: HistoryItemModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .background(Color.Unspecified, shape = MaterialTheme.shapes.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_history_checked),
            contentDescription = "Check",
            tint =  Color.Unspecified,
            modifier = Modifier.padding(top = 3.dp, end = 8.dp, bottom = 3.dp),
        )

        Text(
            text = item.content,
            color = Gray00,
            style = PoptatoTypo.smMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun LazyListState.onLoadMoreWhenLastItemVisible(action: () -> Unit) {
    val reached by remember {
        derivedStateOf {
            reachedLastItem()
        }
    }
    LaunchedEffect(reached) {
        if (reached) action()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfinityLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    loadMore: () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    state.onLoadMoreWhenLastItemVisible(action = loadMore)
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        LazyColumn(
            modifier = modifier.padding(start = 10.dp, end = 10.dp),
            state = state,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content
        )
    }
}