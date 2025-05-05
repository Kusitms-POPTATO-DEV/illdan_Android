package com.poptato.yesterdaylist

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.component.PoptatoButton
import com.poptato.design_system.Complete
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray10
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary40
import com.poptato.design_system.R
import com.poptato.design_system.YesterdayListTitle
import com.poptato.domain.model.enums.TodoStatus
import com.poptato.domain.model.response.yesterday.YesterdayItemModel
import com.poptato.ui.common.PoptatoCheckBox

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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            YesterdayTopBar(
                onClickBtnComplete = onClickCloseBtn
            )

            Spacer(modifier = Modifier.height(16.dp))

            YesterdayTodoList(
                list = uiState.yesterdayList,
                modifier = Modifier.weight(1f),
                onCheckedChange = onCheckedChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            PoptatoButton(
                buttonText = Complete,
                backgroundColor = Primary40,
                onClickButton = onClickBtnComplete
            )
        }
    }
}

@Composable
fun YesterdayTopBar(
    onClickBtnComplete: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = YesterdayListTitle,
            style = PoptatoTypo.mdSemiBold,
            color = Gray00
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_close_no_bg),
                contentDescription = null,
                tint = Gray00,
                modifier = Modifier
                    .clickable { onClickBtnComplete() }
            )
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun YesterdayTodoList(
    list: List<YesterdayItemModel> = emptyList(),
    modifier: Modifier = Modifier,
    onCheckedChange: (Long, TodoStatus) -> Unit = {_, _ ->},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        items(list, key = { it.todoId }) { item ->
            YesterdayTodoItem(
                item = item,
                onCheckedChange = onCheckedChange
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun YesterdayTodoItem(
    item: YesterdayItemModel = YesterdayItemModel(),
    onCheckedChange: (Long, TodoStatus) -> Unit = {_, _ ->},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Gray95)
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        PoptatoCheckBox(
            isChecked = item.todoStatus == TodoStatus.COMPLETED,
            onCheckedChange = { onCheckedChange(item.todoId, item.todoStatus) }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.content,
            color = Gray10,
            style = PoptatoTypo.mdRegular,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewYesterdayList() {
    YesterdayContent()
}