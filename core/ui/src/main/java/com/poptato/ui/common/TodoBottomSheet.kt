package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.poptato.design_system.Gray95
import com.poptato.design_system.Settings
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
    onClickBtnRepeat: (Long) -> Unit = {}
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
        onClickBtnRepeat = onClickBtnRepeat
    )
}

@Composable
fun TodoBottomSheetContent(
    item: TodoItemModel = TodoItemModel(),
    categoryItem: CategoryItemModel? = CategoryItemModel(),
    onClickShowDatePicker: () -> Unit = {},
    onClickBtnDelete: (Long) -> Unit = {},
    onClickBtnModify: (Long) -> Unit = {},
    onClickBtnBookmark: (Long) -> Unit = {},
    onClickCategoryBottomSheet: () -> Unit = {},
    onClickBtnRepeat: (Long) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(324.dp)
            .background(Gray95)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray95)
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
                    tint = Color.Unspecified,
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

        Spacer(modifier = Modifier.height(16.dp))

        BottomSheetBtn(resourceId = R.drawable.ic_pen, buttonText = modify, textColor = Gray30, modifier = Modifier.clickable {
            onClickBtnModify(item.todoId)
            AnalyticsManager.logEvent(
                eventName = "edit_task"
            )
        })
        BottomSheetBtn(resourceId = R.drawable.ic_trash, buttonText = DELETE_ACTION, textColor = Danger50, modifier = Modifier.clickable {
            onClickBtnDelete(item.todoId)
            AnalyticsManager.logEvent(
                eventName = "delete_task"
            )
        })
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
            resourceId = R.drawable.ic_calendar,
            buttonText = DEADLINE_OPTION,
            textColor = Gray30,
            modifier = Modifier.clickable {
                onClickShowDatePicker()
            },
            deadline = item.deadline
        )
        BottomSheetBtn(
            resourceId = R.drawable.ic_add_category_icon,
            buttonText = Category,
            textColor = Gray30,
            category = categoryItem,
            modifier = Modifier.clickable {
                onClickCategoryBottomSheet()
            }
        )
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun BottomSheetBtn(
    resourceId: Int,
    resourceColor: Color = Color.Unspecified,
    buttonText: String,
    textColor: Color,
    deadline: String = "",
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
        Text(text = buttonText, style = PoptatoTypo.mdRegular, color = textColor)
        Spacer(modifier = Modifier.weight(1f))
        if (isRepeatBtn) PoptatoSwitchButton(
            check = isRepeat,
            onClick = {
                onClickBtnRepeat()
            }
        )
        if (buttonText == DEADLINE_OPTION && deadline.isNotEmpty()) {
            Text(text = deadline, style = PoptatoTypo.mdMedium, color = Gray00)
        } else if (buttonText == DEADLINE_OPTION) {
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