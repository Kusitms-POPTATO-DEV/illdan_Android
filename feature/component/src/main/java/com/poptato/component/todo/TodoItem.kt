package com.poptato.component.todo

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poptato.domain.model.enums.TodoType
import com.poptato.design_system.BOOKMARK
import com.poptato.design_system.DOT
import com.poptato.design_system.FULL_DAY
import com.poptato.design_system.GENERAL_REPEAT
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray50
import com.poptato.design_system.Gray80
import com.poptato.design_system.Gray90
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary40
import com.poptato.design_system.R
import com.poptato.design_system.REPEAT_TODO
import com.poptato.design_system.TODO_TIME
import com.poptato.domain.model.enums.TodoStatus
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.common.PoptatoCheckBox
import com.poptato.ui.common.formatDeadline
import com.poptato.ui.util.toPx
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.StringFormat

@SuppressLint("ModifierParameter")
@Composable
fun TodoItem(
    item: TodoItemModel,
    isActive: Boolean = false,
    isDeadlineDateMode: Boolean = false,
    modifier: Modifier = Modifier,
    todoType: TodoType,
    showBottomSheet: (TodoItemModel) -> Unit = {},
    onClearActiveItem: () -> Unit = {},
    onTodoItemModified: (Long, String) -> Unit = { _, _ -> },
    onCheckedChange: (TodoStatus, Long) -> Unit = { _, _ -> },
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = item.content,
                selection = TextRange(item.content.length)
            )
        )
    }
    var showAnimation by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = showAnimation, label = "")
    val elevation by transition.animateDp(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) 8.dp else 0.dp
    }

    val scale by transition.animateFloat(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) 1.1f else 1f
    }

    val yOffset by transition.animateFloat(
        transitionSpec = { spring() },
        label = ""
    ) { isAnimating ->
        if (isAnimating) -50f else 0f
    }

    LaunchedEffect(transition) {
        snapshotFlow { transition.currentState }
            .filter { it }
            .collect {
                showAnimation = false
            }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Gray95)
            .padding(vertical = 16.dp)
            .padding(start = 16.dp, end = 12.dp)
            .offset { IntOffset(0, yOffset.toInt()) }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                shadowElevation = elevation.toPx()
            )
    ) {
        if (todoType == TodoType.TODAY) {
            PoptatoCheckBox(
                isChecked = item.todoStatus == TodoStatus.COMPLETED,
                onCheckedChange = {
                    onCheckedChange(item.todoStatus, item.todoId)
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            if (isActive) {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newTextFieldValue ->
                        textFieldValue = newTextFieldValue
                    },
                    textStyle = PoptatoTypo.mdRegular.copy(color = Gray00),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                textFieldValue = textFieldValue.copy(
                                    selection = TextRange(textFieldValue.text.length)
                                )
                            }
                        },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onClearActiveItem()
                            if (item.content != textFieldValue.text) onTodoItemModified(item.todoId, textFieldValue.text)
                        }
                    ),
                    cursorBrush = SolidColor(Gray00)
                )
            } else {
                Text(
                    text = item.content,
                    color = Gray00,
                    style = PoptatoTypo.mdRegular,
                    modifier = Modifier
                        .offset(y = 1.5.dp)
                )
            }

            if (item.isRepeat || item.routineDays.isNotEmpty() || item.dDay != null) {
                Spacer(modifier = Modifier.height(8.dp))
                RepeatDeadlineRow(
                    item = item,
                    isDeadlineDateMode = isDeadlineDateMode
                )
            }

            if (item.isBookmark || item.time.isNotEmpty() || item.categoryName.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                BookmarkTimeCategoryItem(item)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_three_dot),
            contentDescription = null,
            tint = Gray80,
            modifier = Modifier.clickable { showBottomSheet(item) }
        )
    }
}

@Composable
private fun RepeatDeadlineRow(
    item: TodoItemModel,
    isDeadlineDateMode: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.isRepeat) {
            Text(
                text = GENERAL_REPEAT,
                color = Gray50,
                style = PoptatoTypo.xsRegular
            )

            if (item.dDay != null) {
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = DOT,
                    color = Gray50,
                    style = PoptatoTypo.xsRegular
                )

                Spacer(modifier = Modifier.width(4.dp))
            }
        } else {
            if (item.routineDays.isNotEmpty()) {
                Text(
                    text = if (item.routineDays.size == 7) FULL_DAY else item.routineDays.joinToString(""),
                    color = Gray50,
                    style = PoptatoTypo.xsRegular
                )

                if (item.dDay != null) {
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = DOT,
                        color = Gray50,
                        style = PoptatoTypo.xsRegular
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }

        if (item.dDay != null) {
            Text(
                text = if(isDeadlineDateMode) item.deadline else formatDeadline(item.dDay),
                style = PoptatoTypo.xsRegular,
                color = Gray50
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun BookmarkTimeCategoryItem(
    item: TodoItemModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.isBookmark) {
            TodoInfoChip(
                title = BOOKMARK,
                isBookmark = true
            )
            
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (item.time.isNotEmpty()) {
            TodoInfoChip(
                title = String.format(TODO_TIME, item.meridiem, item.hour, item.minute),
                isBookmark = false,
                isTime = true
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        if (item.categoryName.isNotEmpty()) {
            TodoInfoChip(
                image = item.imageUrl,
                title = item.categoryName,
                isBookmark = false
            )
        }
    }
}

@Composable
private fun TodoInfoChip(
    image: String = "",
    title: String,
    isBookmark: Boolean,
    isTime: Boolean = false
) {
    Row(
        modifier = Modifier
            .background(color = Gray90, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isBookmark) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_filled),
                contentDescription = "",
                modifier = Modifier.size(12.dp),
                tint = Primary40
            )
        } else if (isTime) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = "",
                modifier = Modifier.size(12.dp),
                tint = Color.Unspecified
            )
        } else {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                contentScale = ContentScale.Crop
            )
        }
        
        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = title,
            color = if (isBookmark) Primary40 else Gray00,
            style = PoptatoTypo.xsRegular
        )
    }
}