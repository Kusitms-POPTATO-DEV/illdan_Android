package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poptato.design_system.FULL_DAY
import com.poptato.design_system.GENERAL_REPEAT_GUIDE
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray50
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary30
import com.poptato.design_system.Primary40
import com.poptato.design_system.Primary80
import com.poptato.domain.model.enums.RoutineType
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.ui.util.toDayIndexSet

@SuppressLint("MutableCollectionMutableState")
@Composable
fun RoutineBottomSheet(
    item: TodoItemModel,
    onDismissRequest: () -> Unit,
    updateTodoRoutine: (Long, Set<Int>?) -> Unit,
    updateTodoRepeat: (Long, Boolean) -> Unit
) {
    var selectedType by remember { mutableStateOf(RoutineType.WEEKDAY) }
    var selectedWeekDay by remember { mutableStateOf(item.routineDays.toDayIndexSet()) }

    Column(
        modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            RoutineTypeSelector(selectedType = selectedType, onClickButton = { selectedType = it })

            Spacer(modifier = Modifier.height(32.dp))

            when (selectedType) {
                RoutineType.WEEKDAY -> {
                    FullDayToggle(
                        isOn = selectedWeekDay.size == 7,
                        onClickToggle = { selectedWeekDay = if (selectedWeekDay.size == 7) emptySet() else (0..6).toSet() }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    WeekDayList(
                        isFullDaySelected = selectedWeekDay.size == 7,
                        selectedWeekDay = selectedWeekDay,
                        onClickWeekDay = {
                            selectedWeekDay = if (selectedWeekDay.contains(it)) {
                                selectedWeekDay - it
                            } else {
                                selectedWeekDay + it
                            }
                        }
                    )
                }
                RoutineType.GENERAL -> {
                    GeneralRoutineGuideText()
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        BottomSheetActionButtons(
            onDelete = {
                when (selectedType) {
                    RoutineType.WEEKDAY -> { updateTodoRoutine(item.todoId, null) }
                    RoutineType.GENERAL -> { updateTodoRepeat(item.todoId, false) }
                }
                onDismissRequest()
            },
            onConfirm = {
                when (selectedType) {
                    RoutineType.WEEKDAY -> { updateTodoRoutine(item.todoId, selectedWeekDay) }
                    RoutineType.GENERAL -> { updateTodoRepeat(item.todoId, true) }
                }
                onDismissRequest()
            }
        )
    }
}

@Composable
fun RoutineTypeSelector(
    selectedType: RoutineType,
    onClickButton: (RoutineType) -> Unit
) {
    Row {
        RoutineTypeButton(
            type = RoutineType.WEEKDAY,
            isSelected = selectedType == RoutineType.WEEKDAY,
            onClickButton = { onClickButton(RoutineType.WEEKDAY) }
        )

        Spacer(modifier = Modifier.width(8.dp))

        RoutineTypeButton(
            type = RoutineType.GENERAL,
            isSelected = selectedType == RoutineType.GENERAL,
            onClickButton = { onClickButton(RoutineType.GENERAL) }
        )
    }
}

@Composable
fun RoutineTypeButton(
    type: RoutineType,
    isSelected: Boolean,
    onClickButton: (RoutineType) -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) Primary30 else Gray70,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClickButton(type) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = type.value,
            style = PoptatoTypo.smMedium,
            color = if (isSelected) Primary40 else Gray50
        )
    }
}

@Composable
fun FullDayToggle(
    isOn: Boolean,
    onClickToggle: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = FULL_DAY,
            style = PoptatoTypo.mdMedium,
            color = Gray00
        )

        Spacer(modifier = Modifier.width(12.dp))

        PoptatoSwitchButton(
            check = isOn,
            onClick = { onClickToggle(!isOn) }
        )
    }
}

@Composable
fun WeekDayList(
    isFullDaySelected: Boolean,
    selectedWeekDay: Set<Int>,
    onClickWeekDay: (Int) -> Unit
) {
    val weekDays = listOf("월", "화", "수", "목", "금", "토", "일")
    
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDays.forEachIndexed { index, day ->
            val isSelected = isFullDaySelected || selectedWeekDay.contains(index)

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        if (isSelected) Primary40 else Gray95,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onClickWeekDay(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    style = PoptatoTypo.mdMedium,
                    color = if (isSelected) Primary80 else Gray50
                )
            }
        }
    }
}

@Composable
fun GeneralRoutineGuideText() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray95, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = GENERAL_REPEAT_GUIDE,
            style = PoptatoTypo.mdRegular,
            color = Gray50,
            textAlign = TextAlign.Start
        )
    }
}