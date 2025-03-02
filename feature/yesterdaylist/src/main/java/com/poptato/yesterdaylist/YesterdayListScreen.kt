package com.poptato.yesterdaylist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.design_system.Complete
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.R
import com.poptato.design_system.YesterdayAllCheckBtn
import com.poptato.design_system.YesterdayListTitle
import com.poptato.domain.model.enums.TodoStatus
import com.poptato.domain.model.response.yesterday.YesterdayItemModel
import com.poptato.ui.common.BookmarkItem
import com.poptato.ui.common.DeadlineItem
import com.poptato.ui.common.PoptatoCheckBox
import com.poptato.ui.common.RepeatItem
import timber.log.Timber

@Composable
fun YesterdayListScreen(
    goBackToBacklog: () -> Unit = {}
) {

    val viewModel: YesterdayListViewModel = hiltViewModel()
    val uiState: YesterdayListPageState by viewModel.uiState.collectAsStateWithLifecycle()

    YesterdayContent(
        uiState = uiState,
        onClickCloseBtn = { goBackToBacklog() },
        onCheckedChange = { id, status ->
            viewModel.onCheckedTodo(id, status)
        },
        onClickBtnComplete = {
            viewModel.updateYesterdayTodoCompletion()
            goBackToBacklog()
        }
    )
}

@Composable
fun YesterdayContent(
    uiState: YesterdayListPageState = YesterdayListPageState(),
    onClickCloseBtn: () -> Unit = {},
    onClickBtnComplete: () -> Unit  = {},
    onCheckedChange: (Long, TodoStatus) -> Unit = {_, _ ->}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TitleTopBar(
                onClickBtnComplete = onClickCloseBtn
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = YesterdayListTitle,
                    style = PoptatoTypo.xxLSemiBold,
                    color = Gray00,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_yesterday_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp)
                ) {
                    YesterdayTodoList(
                        list = uiState.yesterdayList,
                        onCheckedChange = onCheckedChange
                    )
                }
            }

            BtnComplete(
                onClickAllCheckBtn = onClickBtnComplete
            )
        }

    }
}

@Composable
fun TitleTopBar(
    onClickBtnComplete: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_close_no_bg),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(top = 24.dp, bottom = 8.dp)
                .size(width = 24.dp, height = 24.dp)
                .clickable { onClickBtnComplete() }
        )
    }
}

@Composable
fun YesterdayTodoList(
    list: List<YesterdayItemModel> = emptyList(),
    onCheckedChange: (Long, TodoStatus) -> Unit = {_, _ ->},
) {
    Timber.d("[어제 한 일] list -> $list")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {

        items(list, key = { it.todoId }) { item ->
            Spacer(modifier = Modifier.height(8.dp))

            YesterdayTodoItem(
                item = item,
                onCheckedChange = onCheckedChange
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun YesterdayTodoItem(
    item: YesterdayItemModel = YesterdayItemModel(),
    onCheckedChange: (Long, TodoStatus) -> Unit = {_, _ ->},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gray95)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = if (item.bookmark || item.repeat || item.dday != null) 12.dp else 0.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            if (item.bookmark) {
                BookmarkItem()
                Spacer(modifier = Modifier.width(6.dp))
            }

            if (item.repeat) {
                RepeatItem()
                Spacer(modifier = Modifier.width(6.dp))
            }

            if (item.dday != null) {
                DeadlineItem(
                    dday = item.dday
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(bottom = if (item.bookmark || item.repeat || item.dday != null) 12.dp else 16.dp)
                .padding(top = if (item.bookmark || item.repeat || item.dday != null) 6.dp else 16.dp),

        ) {
            PoptatoCheckBox(
                isChecked = item.todoStatus == TodoStatus.COMPLETED,
                onCheckedChange = { onCheckedChange(item.todoId, item.todoStatus) }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = item.content,
                color = Gray40,
                style = PoptatoTypo.mdRegular,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BtnComplete(
    onClickAllCheckBtn: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .background(Primary60, shape = RoundedCornerShape(12.dp))
            .clickable { onClickAllCheckBtn() }
    ) {
        Text(
            text = Complete,
            style = PoptatoTypo.lgSemiBold,
            color = Gray100,
            modifier = Modifier
                .padding(vertical = 15.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewYesterdayList() {
    YesterdayContent()
}