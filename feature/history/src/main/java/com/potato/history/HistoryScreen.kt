package com.potato.history

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray20
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray80
import com.poptato.design_system.Gray95
import com.poptato.design_system.HistoryListEmpty
import com.poptato.design_system.HistoryTitle
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import com.poptato.domain.model.response.history.HistoryItemModel
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.poptato.core.util.reachedLastItem
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray90
import com.poptato.design_system.PoptatoTypo.xsMedium
import com.potato.history.model.HistoryGroupedItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(

) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val uiState: HistoryPageState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryContent(
        uiState = uiState,
        onLoadNextPage = { viewModel.getHistoryList() }
    )
}

@Composable
fun HistoryContent(
    uiState: HistoryPageState = HistoryPageState(),
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()
    val currentMonth = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {

        CalendarContent(
            currentMonth = currentMonth.value,
            selectedDate = selectedDate.value,
            onSelectedDate = { selectedDate.value = it }
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
                items(uiState.historyList) { groupedItem ->
                    groupedItem.items.forEach { item ->
                        HistoryListItem(item = item)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

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
    currentMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    selectedDate: LocalDate = LocalDate.now(),
    onSelectedDate: (LocalDate) -> Unit = {}
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = (currentMonth.withDayOfMonth(1).dayOfWeek.value % 7) // 일요일 = 0
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray100)
            .padding(start = 24.dp, end = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        CalendarHeader(
            currentMonth = currentMonth,
            onPreviousMonthClick = { onSelectedDate(currentMonth.minusMonths(1)) },
            onNextMonthClick = { onSelectedDate(currentMonth.plusMonths(1)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

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
                val date = currentMonth.withDayOfMonth(day)
                CalendarDayItem(
                    day = day,
                    date = date,
                    isSelected = selectedDate == date,
                    isToday = today == date,
                    hasEvent = day % 8 == 0, // 수정 필요 => api로부터의 데이터
                    onClick = { onSelectedDate(date) }
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: LocalDate,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(24.dp)
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonthClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left_20),
                contentDescription = "Previous Month",
                tint = Gray40
            )
        }
        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월")),
            style = PoptatoTypo.mdMedium,
            color = Gray00
        )
        IconButton(onClick = onNextMonthClick) {
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
        listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
            Text(
                text = day,
                style = PoptatoTypo.smMedium,
                fontSize = 10.sp,
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
    onClick: () -> Unit
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
            if(date.isAfter(LocalDate.now())){
                Image(
                    painter = painterResource(id = R.drawable.ic_history_circle),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                        .padding(1.dp)
                )
            }else if(hasEvent){
                Image(
                    painter = painterResource(id = R.drawable.ic_history_star),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }else if(date.isEqual(LocalDate.now())){
                Image(
                    painter = painterResource(id = R.drawable.ic_history_circle),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.ic_history_moon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
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
            modifier = Modifier.weight(1f)
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

    LazyColumn(
        modifier = modifier,
        state = state,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHistoryScreen() {
    val dummyUiState = getDummyHistoryPageState()

    HistoryContent(
        uiState = dummyUiState,
        onLoadNextPage = {} // 더미
    )
}

private fun getDummyHistoryPageState(): HistoryPageState {
    return HistoryPageState(
        historyList = listOf(
            HistoryGroupedItem(
                date = "2024-11-18",
                items = listOf(
                    HistoryItemModel(
                        todoId = 1,
                        content = "더미 콘텐츠 1",
                        date = "2024-11-18"
                    ),
                    HistoryItemModel(
                        todoId = 2,
                        content = "더미 콘텐츠 2",
                        date = "2024-11-18"
                    )
                )
            )
        )
    )
}