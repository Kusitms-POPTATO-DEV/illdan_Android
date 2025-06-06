package com.poptato.feature

import androidx.lifecycle.viewModelScope
import com.poptato.core.enums.BottomNavType
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
    val todoEventFlow = MutableSharedFlow<TodoExternalEvent>()
    val animationDuration = 300
    val selectedIconInBottomSheet = MutableSharedFlow<CategoryIconItemModel>()
    val categoryScreenContent = MutableSharedFlow<CategoryScreenContentModel>(replay = 1)
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

        updateState(
            uiState.value.copy(
                selectedTodoItem = item,
                categoryList = category,
                selectedTodoCategoryItem = categoryItemModel
            )
        )
        emitEventFlow(MainEvent.ShowTodoBottomSheet)
    }

    fun onUpdatedCategory(selectedId: Long?) {
        val selectedCategory = uiState.value.categoryList.firstOrNull { it.categoryId == selectedId }
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
    }

    fun onUpdatedDeadline(date: String?) {
        val updatedItem = uiState.value.selectedTodoItem.copy(deadline = date ?: "")

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )
    }

    fun onUpdatedBookmark(value: Boolean) {
        val updatedItem = uiState.value.selectedTodoItem.copy(isBookmark = value)

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )
    }

    fun onUpdatedTodoRepeat(value: Boolean) {
        val updatedItem = uiState.value.selectedTodoItem.copy(isRepeat = value)

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )
    }

    fun onUpdatedTodoRoutine(days: Set<Int>?) {
        val request = RoutineRequestModel()
        request.convertIndexToDays(days?.toList())
        val updatedItem = uiState.value.selectedTodoItem.copy(routineDays = request.routineDays ?: emptyList())

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )
    }

    fun onUpdatedTodoTime(value: Triple<String, Int, Int>?) {
        val time = uiState.value.selectedTodoItem.formatTime(value)
        val updatedItem = uiState.value.selectedTodoItem.copy(time = time)

        updateState(
            uiState.value.copy(
                selectedTodoItem = updatedItem
            )
        )

        AnalyticsManager.logEvent(eventName = "set_time")
    }

    fun toggleBackPressed(value: Boolean) { updateState(uiState.value.copy(backPressedOnce = value)) }

    fun updateBottomSheetType(type: BottomSheetType) { updateState(uiState.value.copy(bottomSheetType = type)) }

    fun onSelectedCategoryIcon(categoryList: CategoryIconTotalListModel) {
        updateState(
            uiState.value.copy(
                bottomSheetType = BottomSheetType.CategoryIcon,
                categoryIconList = categoryList
            )
        )
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
}