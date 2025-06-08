package com.poptato.feature

import androidx.lifecycle.viewModelScope
import com.poptato.core.enums.BottomNavType
import com.poptato.core.util.DateTimeFormatter
import com.poptato.domain.model.enums.BottomSheetType
import com.poptato.domain.model.request.ListRequestModel
import com.poptato.domain.model.request.todo.RoutineRequestModel
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.model.response.dialog.DialogContentModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.domain.model.response.yesterday.YesterdayListModel
import com.poptato.domain.usecase.yesterday.GetYesterdayListUseCase
import com.poptato.navigation.NavRoutes
import com.poptato.ui.base.BaseViewModel
import com.poptato.ui.event.TodoExternalEvent
import com.poptato.ui.util.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getYesterdayListUseCase: GetYesterdayListUseCase
) : BaseViewModel<MainPageState>(MainPageState()) {
    val todoEventFlow = MutableSharedFlow<TodoExternalEvent>(replay = 1)
    val selectedIconInBottomSheet = MutableSharedFlow<CategoryIconItemModel>()
    val categoryScreenContentFromBacklog = MutableSharedFlow<CategoryScreenContentModel>(replay = 1)
    val userDeleteName = MutableSharedFlow<String>(replay = 1)

    fun setBottomNavType(route: String?) {
        val type = when (route) {
            NavRoutes.BacklogScreen.route -> {
                BottomNavType.BACK_LOG
            }
            NavRoutes.TodayScreen.route -> {
                BottomNavType.TODAY
            }
            NavRoutes.MyPageScreen.route, NavRoutes.SettingScreen.route, NavRoutes.UserDataScreen.route -> {
                BottomNavType.SETTINGS
            }
            NavRoutes.HistoryScreen.route -> {
                BottomNavType.HISTORY
            }
            else -> {
                BottomNavType.DEFAULT
            }
        }
        updateBottomNav(type)
    }

    private fun updateBottomNav(type: BottomNavType) {
        updateState(
            uiState.value.copy(
                bottomNavType = type
            )
        )
    }

    fun onSelectedTodoItem(item: TodoItemModel, category: List<CategoryItemModel>) {
        val isAllOrStar: Boolean = item.categoryId.toInt() == -1 || item.categoryId.toInt() == 0
        val categoryItemModel = if (isAllOrStar) null else category.firstOrNull { it.categoryId == item.categoryId }

        updateStateSync(
            uiState.value.copy(
                selectedTodoItem = item,
                categoryList = category,
                selectedTodoCategoryItem = categoryItemModel
            )
        )

        updateBottomSheetType(BottomSheetType.Main)
    }

    fun toggleBackPressed(value: Boolean) { updateState(uiState.value.copy(backPressedOnce = value)) }

    fun onSelectedCategoryIcon(categoryList: CategoryIconTotalListModel) {
        updateState(
            uiState.value.copy(
                bottomSheetType = BottomSheetType.CategoryIcon,
                categoryIconList = categoryList
            )
        )

        updateBottomSheetType(BottomSheetType.CategoryIcon)
    }

    fun onSetDialogContent(dialogContent: DialogContentModel) {
        updateState(
            uiState.value.copy(
                dialogContent = dialogContent
            )
        )
    }

    fun getYesterdayList() {
        viewModelScope.launch {
            getYesterdayListUseCase(ListRequestModel(0, 1)).collect {
                resultResponse(it, ::onSuccessGetYesterdayList)
            }
        }
    }

    private fun onSuccessGetYesterdayList(result: YesterdayListModel) {
        updateState(uiState.value.copy(isExistYesterday = result.yesterdays.isNotEmpty()))
    }

    // GA 이벤트 기록 메서드
    fun logAnalyticsEventForRoute(route: String) {
        if (route == NavRoutes.TodayScreen.route) { AnalyticsManager.logEvent(eventName = "get_today") }
        else if (route == NavRoutes.HistoryScreen.route) { AnalyticsManager.logEvent(eventName = "get_calendar") }
    }

    // BottomSheetHandler
    fun updateBottomSheetType(type: BottomSheetType) { updateState(uiState.value.copy(bottomSheetType = type)) }

    fun onDeleteTodo(todoId: Long) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.DeleteTodo(todoId)) }
        updateBottomSheetType(BottomSheetType.None)
    }

    fun onModifyTodo(todoId: Long) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.ActiveItem(todoId)) }
        updateBottomSheetType(BottomSheetType.None)
    }

    fun onUpdateTodoBookmark(todoId: Long, value: Boolean) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.UpdateBookmark(todoId)) }
        val updatedItem = uiState.value.selectedTodoItem.copy(isBookmark = value)

        updateState(uiState.value.copy(selectedTodoItem = updatedItem))

        AnalyticsManager.logEvent(
            eventName = "set_important",
            params = mapOf("task_ID" to "${uiState.value.selectedTodoItem.todoId}")
        )
    }

    fun onUpdatedTodoDeadline(date: String?) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.UpdateDeadline(date)) }
        val updatedItem = uiState.value.selectedTodoItem.copy(deadline = date ?: "")

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )

        AnalyticsManager.logEvent(
            eventName = "set_dday",
            params = mapOf("set_date" to DateTimeFormatter.getTodayFullDate(), "dday" to "$date", "task_ID" to "${uiState.value.selectedTodoItem.todoId}")
        )
    }

    fun onCategoryIconSelected(selectedIcon: CategoryIconItemModel) {
        viewModelScope.launch { selectedIconInBottomSheet.emit(selectedIcon) }
        updateBottomSheetType(BottomSheetType.None)
    }

    fun onCategorySelected(id: Long?) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.UpdateCategory(id)) }

        val selectedCategory = uiState.value.categoryList.firstOrNull { it.categoryId == id }
        val updatedTodo = uiState.value.selectedTodoItem.copy(
            categoryName = selectedCategory?.categoryName ?: "",
            imageUrl = selectedCategory?.categoryImgUrl ?: ""
        )

        updateState(
            uiState.value.copy(
                selectedTodoCategoryItem = selectedCategory,
                selectedTodoItem = updatedTodo
            )
        )

        AnalyticsManager.logEvent(
            eventName = "set_category"
        )
    }

    fun onUpdateTodoTime(todoId: Long, value: Triple<String, Int, Int>?) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.UpdateTime(Pair(todoId, uiState.value.selectedTodoItem.formatTime(value)))) }

        val time = uiState.value.selectedTodoItem.formatTime(value)
        val updatedItem = uiState.value.selectedTodoItem.copy(time = time)

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )

        AnalyticsManager.logEvent(eventName = "set_time")
    }

    fun onUpdateTodoRepeat(todoId: Long, value: Boolean) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.UpdateRepeat(todoId, value)) }

        val updatedItem = uiState.value.selectedTodoItem.copy(
            isRepeat = value,
            routineDays = if (value) emptyList() else uiState.value.selectedTodoItem.routineDays
        )

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )
    }

    fun onUpdateTodoRoutine(todoId: Long, days: Set<Int>?) {
        viewModelScope.launch { todoEventFlow.emit(TodoExternalEvent.UpdateRoutine(todoId, days)) }

        val request = RoutineRequestModel()
        request.convertIndexToDays(days?.toList())
        val updatedItem = uiState.value.selectedTodoItem.copy(
            routineDays = request.routineDays ?: emptyList(),
            isRepeat = if (request.routineDays?.isNotEmpty() == true) false else uiState.value.selectedTodoItem.isRepeat
        )

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )
    }
}

























