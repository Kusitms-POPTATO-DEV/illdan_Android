package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.poptato.design_system.Category
import com.poptato.design_system.DEADLINE_OPTION
import com.poptato.design_system.DELETE_ACTION
import com.poptato.design_system.Danger50
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray30
import com.poptato.design_system.Gray60
import com.poptato.design_system.Gray90
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import com.poptato.design_system.REPEAT_TASK_OPTION
import com.poptato.design_system.DELETE_ACTION
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray95
import com.poptato.design_system.Primary40
import com.poptato.design_system.Settings
import com.poptato.design_system.TIME
import com.poptato.design_system.TODO_TIME
import com.poptato.design_system.WORD_DATE
import com.poptato.design_system.modify
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.util.AnalyticsManager

@Composable
fun TodoBottomSheet(
    item: TodoItemModel = TodoItemModel(),
    categoryItem: CategoryItemModel? = CategoryItemModel(),
    onClickShowDatePicker: () -> Unit = {},
    onClickBtnDelete: (Long) -> Unit = {},
    onClickBtnModify: (Long) -> Unit = {},
    onClickBtnBookmark: (Long) -> Unit = {},
    onClickCategoryBottomSheet: () -> Unit = {},
    onClickBtnRepeat: (Long) -> Unit = {},
    onClickBtnTime: (Long) -> Unit = {}
) {
    var deadline by remember { mutableStateOf(item.deadline) }
    var isBookmark by remember { mutableStateOf(item.isBookmark) }
    var isRepeat by remember { mutableStateOf(item.isRepeat) }

    LaunchedEffect(item) {
        deadline = item.deadline
        isBookmark = item.isBookmark
        isRepeat = item.isRepeat
    }

    TodoBottomSheetContent(
        categoryItem = categoryItem,
        item = item.copy(deadline = deadline, isBookmark = isBookmark, isRepeat = isRepeat),
        onClickShowDatePicker = onClickShowDatePicker,
        onClickBtnDelete = onClickBtnDelete,
        onClickBtnModify = onClickBtnModify,
        onClickBtnBookmark = {
            onClickBtnBookmark(it)
            isBookmark = !isBookmark
        },
        onClickCategoryBottomSheet = onClickCategoryBottomSheet,
        onClickBtnRepeat = onClickBtnRepeat,
        onClickBtnTime = onClickBtnTime
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun TodoBottomSheetContent(
    item: TodoItemModel = TodoItemModel(),
    categoryItem: CategoryItemModel? = CategoryItemModel(),
    onClickShowDatePicker: () -> Unit = {},
    onClickBtnDelete: (Long) -> Unit = {},
    onClickBtnModify: (Long) -> Unit = {},
    onClickBtnBookmark: (Long) -> Unit = {},
    onClickCategoryBottomSheet: () -> Unit = {},
    onClickBtnRepeat: (Long) -> Unit = {},
    onClickBtnTime: (Long) -> Unit = {}
) {
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
                .padding(horizontal = 24.dp)
        ) {

            Text(
                text = item.content,
                style = PoptatoTypo.lgMedium,
                color = Gray00,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            if (item.isBookmark) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_filled),
                    contentDescription = "",
                    tint = Primary40,
                    modifier = Modifier.clickable { onClickBtnBookmark(item.todoId) }
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_empty),
                    contentDescription = "",
                    tint = Gray60,
                    modifier = Modifier.clickable { onClickBtnBookmark(item.todoId) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Gray95, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp)
                    .clickable { onClickBtnDelete(item.todoId) },
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = DELETE_ACTION,
                    style = PoptatoTypo.mdRegular,
                    color = Danger50
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Gray95, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp)
                    .clickable { onClickBtnModify(item.todoId) },
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pen),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                
                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = modify,
                    style = PoptatoTypo.mdRegular,
                    color = Gray30
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        BottomSheetBtn(
            resourceId = R.drawable.ic_calendar,
            buttonText = WORD_DATE,
            textColor = Gray30,
            modifier = Modifier.clickable {
                onClickShowDatePicker()
            },
            deadline = item.deadline
        )
        BottomSheetBtn(
            resourceId = R.drawable.ic_clock,
            buttonText = TIME,
            textColor = Gray30,
            time = if (item.time.isNotEmpty()) String.format(TODO_TIME, item.meridiem, item.hour, item.minute) else "",
            modifier = Modifier.clickable {
                onClickBtnTime(item.todoId)
            }
        )
        BottomSheetBtn(
            resourceId = R.drawable.ic_refresh,
            buttonText = REPEAT_TASK_OPTION,
            textColor = Gray30,
            modifier = Modifier.clickable {
                onClickBtnRepeat(item.todoId)
                AnalyticsManager.logEvent(
                    eventName = "set_repeat",
                    params = mapOf("task_ID" to "${item.todoId}")
                )
            },
            isRepeatBtn = true,
            isRepeat = item.isRepeat,
            onClickBtnRepeat = { onClickBtnRepeat(item.todoId) }
        )
        BottomSheetBtn(
            resourceId = R.drawable.ic_add_category,
            buttonText = Category,
            textColor = Gray30,
            category = categoryItem,
            modifier = Modifier.clickable {
                onClickCategoryBottomSheet()
            }
        )
    }
}

@SuppressLint("ModifierParameter", "DefaultLocale")
@Composable
fun BottomSheetBtn(
    resourceId: Int,
    resourceColor: Color = Color.Unspecified,
    buttonText: String,
    textColor: Color,
    deadline: String = "",
    time: String = "",
    category: CategoryItemModel? = CategoryItemModel(),
    modifier: Modifier = Modifier,
    isRepeatBtn: Boolean = false,
    isRepeat: Boolean = false,
    onClickBtnRepeat: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Icon(painter = painterResource(id = resourceId), contentDescription = null, tint = resourceColor, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = buttonText, style = PoptatoTypo.mdMedium, color = textColor)
        Spacer(modifier = Modifier.weight(1f))
        if (isRepeatBtn) PoptatoSwitchButton(
            check = isRepeat,
            onClick = {
                onClickBtnRepeat()
            }
        )
        if (buttonText == WORD_DATE && deadline.isNotEmpty()) {
            Text(text = deadline, style = PoptatoTypo.mdRegular, color = Gray40)
        } else if (buttonText == TIME && time.isNotEmpty()) {
            Text(text = time, style = PoptatoTypo.mdRegular, color = Gray40)
        } else if (buttonText == WORD_DATE || buttonText == TIME) {
            Text(text = Settings, style = PoptatoTypo.mdRegular, color = Gray60)
        }

        CategoryTodoItem(buttonText = buttonText, category = category, modifier = modifier)
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun CategoryTodoItem(
    buttonText: String,
    category: CategoryItemModel? = CategoryItemModel(),
    modifier: Modifier = Modifier
) {

    val categoryFixedIcon: List<Int> = listOf(R.drawable.ic_category_all, R.drawable.ic_category_star)

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    Coil.setImageLoader(imageLoader)

    if (buttonText == Category && category != null) {
        val categoryId: Int = category.categoryId.toInt()
        val categoryIndex: Int = if (categoryId == -1 || categoryId == 0) categoryId + 1 else -1

        Row(
            modifier = modifier
                .clip(RoundedCornerShape(32.dp))
                .background(Gray90)
                .padding(horizontal = 10.dp, vertical = 4.dp),
        ) {
            AsyncImage(
                model = if (categoryIndex == 0 || categoryIndex == 1) categoryFixedIcon[categoryIndex] else category.categoryImgUrl,
                contentDescription = "category icon",
                modifier = Modifier
                    .size(20.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = category.categoryName,
                style = PoptatoTypo.mdMedium,
                color = Gray00
            )
        }
    } else if (buttonText == Category) {
        Text(text = Settings, style = PoptatoTypo.mdRegular, color = Gray60)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomSheet() {
    TodoBottomSheetContent()
}