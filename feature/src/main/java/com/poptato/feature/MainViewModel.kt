package com.poptato.feature

import com.poptato.core.enums.BottomNavType
import com.poptato.domain.model.enums.BottomSheetType
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.model.response.dialog.DialogContentModel
import com.poptato.domain.model.response.history.CalendarMonthModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.navigation.NavRoutes
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainPageState>(MainPageState()) {
    val updateDeadlineFlow = MutableSharedFlow<String?>()
    val deleteTodoFlow = MutableSharedFlow<Long>()
    val activateItemFlow = MutableSharedFlow<Long>()
    val updateBookmarkFlow = MutableSharedFlow<Long>()
    val updateCategoryFlow = MutableSharedFlow<Long?>()
    val updateTodoRepeatFlow = MutableSharedFlow<Long>()
    val animationDuration = 300
    val selectedIconInBottomSheet = MutableSharedFlow<CategoryIconItemModel>()
    val updateMonthFlow = MutableSharedFlow<CalendarMonthModel>()
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
        updateState(
            uiState.value.copy(
                selectedTodoCategoryItem = uiState.value.categoryList.firstOrNull { it.categoryId == selectedId }
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

    fun showMonthPicker(currentMonthModel: CalendarMonthModel) {
        updateState(
            uiState.value.copy(
                selectedMonth = currentMonthModel, // 초기 Month 설정
                bottomSheetType = BottomSheetType.MonthPicker
            )
        )
    }

    fun onMonthSelected(selectedMonthModel: CalendarMonthModel) {
            updateState(
                uiState.value.copy(
                    selectedMonth = selectedMonthModel,
                    bottomSheetType = BottomSheetType.Main
                )
            )
    }
}