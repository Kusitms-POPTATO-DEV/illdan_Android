package com.poptato.today

import androidx.lifecycle.viewModelScope
import com.poptato.core.enums.TodoType
import com.poptato.core.util.TimeFormatter
import com.poptato.core.util.move
import com.poptato.domain.model.enums.TodoStatus
import com.poptato.domain.model.request.category.GetCategoryListRequestModel
import com.poptato.domain.model.request.today.GetTodayListRequestModel
import com.poptato.domain.model.request.todo.DeadlineContentModel
import com.poptato.domain.model.request.todo.DragDropRequestModel
import com.poptato.domain.model.request.todo.ModifyTodoRequestModel
import com.poptato.domain.model.request.todo.TodoCategoryIdModel
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.model.request.todo.UpdateDeadlineRequestModel
import com.poptato.domain.model.request.todo.UpdateTodoCategoryModel
import com.poptato.domain.model.response.category.CategoryListModel
import com.poptato.domain.model.response.today.TodayListModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.domain.model.response.todo.TodoDetailItemModel
import com.poptato.domain.usecase.category.GetCategoryListUseCase
import com.poptato.domain.usecase.today.GetTodayListUseCase
import com.poptato.domain.usecase.todo.DeleteTodoUseCase
import com.poptato.domain.usecase.todo.DragDropUseCase
import com.poptato.domain.usecase.todo.GetTodoDetailUseCase
import com.poptato.domain.usecase.todo.ModifyTodoUseCase
import com.poptato.domain.usecase.todo.SwipeTodoUseCase
import com.poptato.domain.usecase.todo.UpdateBookmarkUseCase
import com.poptato.domain.usecase.todo.UpdateDeadlineUseCase
import com.poptato.domain.usecase.todo.UpdateTodoCategoryUseCase
import com.poptato.domain.usecase.todo.UpdateTodoCompletionUseCase
import com.poptato.domain.usecase.todo.UpdateTodoRepeatUseCase
import com.poptato.ui.base.BaseViewModel
import com.poptato.ui.util.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getTodayListUseCase: GetTodayListUseCase,
    private val updateTodoCompletionUseCase: UpdateTodoCompletionUseCase,
    private val swipeTodoUseCase: SwipeTodoUseCase,
    private val dragDropUseCase: DragDropUseCase,
    private val modifyTodoUseCase: ModifyTodoUseCase,
    private val updateDeadlineUseCase: UpdateDeadlineUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    private val getTodoDetailUseCase: GetTodoDetailUseCase,
    private val updateTodoCategoryUseCase: UpdateTodoCategoryUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoRepeatUseCase: UpdateTodoRepeatUseCase
) : BaseViewModel<TodayPageState>(TodayPageState()) {
    private var snapshotList: List<TodoItemModel> = emptyList()

    init {
        getTodayList(0, 50)
        getCategoryList()
    }

    private fun getCategoryList() {
        viewModelScope.launch {
            getCategoryListUseCase(request = GetCategoryListRequestModel(0, 100)).collect {
                resultResponse(it, { data ->
                    onSuccessGetCategoryList(data)
                })
            }
        }
    }

    private fun onSuccessGetCategoryList(response: CategoryListModel) {
        updateState(
            uiState.value.copy(
                categoryList = response.categoryList
            )
        )
    }

    fun onCheckedTodo(status: TodoStatus, id: Long) {
        updateTodoStatusInUI(status = status, id = id)

        AnalyticsManager.logEvent(
            eventName = "complete_task",
            params = mapOf("task_ID" to "$id")
        )

        viewModelScope.launch {
            updateTodoCompletionUseCase(id).collect {
                resultResponse(it, { updateSnapshotList(uiState.value.todayList) }, { onFailedUpdateTodayList() })
            }
        }
    }

    private fun updateTodoStatusInUI(status: TodoStatus, id: Long) {
        val newStatus = if(status == TodoStatus.COMPLETED) TodoStatus.INCOMPLETE else TodoStatus.COMPLETED
        val selectedItem = uiState.value.todayList.find { it.todoId == id }?.copy(todoStatus = newStatus)
        val remainingItems = uiState.value.todayList.filter { it.todoId != id }
        val newTodays = if (selectedItem != null) {
            val incompleteItems = remainingItems.filter { it.todoStatus == TodoStatus.INCOMPLETE }.toMutableList()
            val completeItems = remainingItems.filter { it.todoStatus == TodoStatus.COMPLETED }.toMutableList()

            if (selectedItem.todoStatus == TodoStatus.COMPLETED) {
                completeItems.add(selectedItem)
            } else {
                incompleteItems.add(selectedItem)
            }

            incompleteItems + completeItems
        } else {
            remainingItems
        }

        updateList(newTodays)
        checkForAllDone(newTodays)
    }

    private fun checkForAllDone(list: List<TodoItemModel>) {
        var isAllChecked = true

        for (item in list) {
            if (item.todoStatus == TodoStatus.INCOMPLETE) {
                isAllChecked = false
                break
            }
        }

        if (isAllChecked) {
            emitEventFlow(TodayEvent.TodayAllChecked)

            AnalyticsManager.logEvent(eventName = "complete_all")
        }
    }

    private fun getTodayList(page: Int, size: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            getTodayListUseCase.invoke(request = GetTodayListRequestModel(page = page, size = size)).collect {
                resultResponse(it, ::onSuccessGetTodayList, { onFailedUpdateTodayList() })
            }
        }
    }

    private fun onSuccessGetTodayList(response: TodayListModel) {
        updateSnapshotList(response.todays)

        updateState(
            uiState.value.copy(
                todayList = response.todays,
                totalPageCount = response.totalPageCount,
                isFinishedInitialization = true
            )
        )
    }

    private fun onFailedUpdateTodayList() {
        updateList(snapshotList)
        emitEventFlow(TodayEvent.OnFailedUpdateTodayList)
    }

    private fun updateList(newList: List<TodoItemModel>) {
        updateState(
            uiState.value.copy(
                todayList = newList
            )
        )
    }

    fun swipeTodayItem(item: TodoItemModel) {
        val newList = uiState.value.todayList.filter { it.todoId != item.todoId }
        updateList(newList)

        AnalyticsManager.logEvent(
            eventName = "back_tasks",
            params = mapOf("task_ID" to "${item.todoId}")
        )

        viewModelScope.launch {
            swipeTodoUseCase.invoke(TodoIdModel(item.todoId)).collect {
                resultResponse(it, { updateSnapshotList(uiState.value.todayList) }, { onFailedUpdateTodayList() })
            }
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        val currentList = uiState.value.todayList.toMutableList()
        val lastIncompleteIndex = currentList.indexOfLast { it.todoStatus == TodoStatus.INCOMPLETE }
        var safeToIndex = toIndex

        if (lastIncompleteIndex in fromIndex..<toIndex) {
            safeToIndex = lastIncompleteIndex
        } else if (lastIncompleteIndex in toIndex..<fromIndex) {
            safeToIndex = lastIncompleteIndex + 1
        }

        if (fromIndex != safeToIndex) {
            AnalyticsManager.logEvent(
                eventName = "drag_today",
                params = mapOf("task_ID" to "${uiState.value.todayList[fromIndex].todoId}")
            )

            currentList.move(fromIndex, safeToIndex)
            updateList(currentList)
        }
    }

    fun onDragEnd() {
        val todoIdList = uiState.value.todayList
            .filter { it.todoStatus == TodoStatus.INCOMPLETE }
            .map { it.todoId }

        viewModelScope.launch {
            dragDropUseCase.invoke(
                request = DragDropRequestModel(
                    type = TodoType.TODAY,
                    todoIds = todoIdList
                )
            ).collect {
                resultResponse(it, { updateSnapshotList(uiState.value.todayList) }, { onFailedUpdateTodayList() })
            }
        }
    }

    private fun updateSnapshotList(newList: List<TodoItemModel>) {
        snapshotList = newList
    }

    fun modifyTodo(item: ModifyTodoRequestModel) {
        modifyTodoInUI(item = item)

        viewModelScope.launch {
            modifyTodoUseCase.invoke(
                request = item
            ).collect {
                resultResponse(it, { updateSnapshotList(uiState.value.todayList) }, { onFailedUpdateTodayList() })
            }
        }
    }

    private fun modifyTodoInUI(item: ModifyTodoRequestModel) {
        val newList = uiState.value.todayList.map {
            if (it.todoId == item.todoId) {
                it.copy(content = item.content.content)
            } else {
                it
            }
        }

        updateList(newList)
    }

    fun setDeadline(deadline: String?, id: Long) {
        updateDeadlineInUI(deadline = deadline, id = id)

        viewModelScope.launch {
            updateDeadlineUseCase.invoke(
                request = UpdateDeadlineRequestModel(
                    todoId = uiState.value.selectedItem.todoId,
                    deadline = DeadlineContentModel(deadline)
                )
            ).collect {
                resultResponse(it, { updateSnapshotList(uiState.value.todayList) }, { onFailedUpdateTodayList() })
            }
        }
    }

    private fun updateDeadlineInUI(deadline: String?, id: Long) {
        val dDay = TimeFormatter.calculateDDay(deadline)
        val newList = uiState.value.todayList.map {
            if (it.todoId == id) {
                it.copy(deadline = deadline ?: "", dDay = dDay)
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(deadline = deadline ?: "", dDay = dDay)

        updateState(
            uiState.value.copy(
                todayList = newList,
                selectedItem = updatedItem
            )
        )
    }

    fun deleteBacklog(id: Long) {
        val newList = uiState.value.todayList.filter { it.todoId != id }
        updateList(newList)

        viewModelScope.launch {
            deleteTodoUseCase.invoke(id).collect {
                resultResponse(
                    it,
                    {
                        updateSnapshotList(uiState.value.todayList)
                        emitEventFlow(TodayEvent.OnSuccessDeleteTodo)
                    },
                    { onFailedUpdateTodayList() }
                )
            }
        }
    }

    fun updateBookmark(id: Long) {
        updateBookmarkInUI(id)

        viewModelScope.launch {
            updateBookmarkUseCase.invoke(id).collect {
                resultResponse(it, { updateSnapshotList(uiState.value.todayList) }, { onFailedUpdateTodayList() })
            }
        }
    }

    private fun updateBookmarkInUI(id: Long) {
        val newList = uiState.value.todayList.map {
            if (it.todoId == id) {
                it.copy(isBookmark = !it.isBookmark)
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(isBookmark = !uiState.value.selectedItem.isBookmark)

        updateState(
            uiState.value.copy(
                todayList = newList,
                selectedItem = updatedItem
            )
        )
    }

    fun updateTodoRepeat(id: Long) {
        updateTodoRepeatInUI(id)

        viewModelScope.launch {
            updateTodoRepeatUseCase(id).collect {
                resultResponse(it, { updateSnapshotList(uiState.value.todayList) }, { onFailedUpdateTodayList() })
            }
        }
    }

    private fun updateTodoRepeatInUI(id: Long) {
        val newList = uiState.value.todayList.map {
            if (it.todoId == id) {
                it.copy(isRepeat = !it.isRepeat)
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(isRepeat = !uiState.value.selectedItem.isRepeat)

        updateState(
            uiState.value.copy(
                todayList = newList,
                selectedItem = updatedItem
            )
        )
    }

    fun getSelectedItemDetailContent(item: TodoItemModel, callback: (TodoItemModel) -> Unit) {
        viewModelScope.launch {
            getTodoDetailUseCase(item.todoId).collect {
                resultResponse(it, { data ->
                    callback(setSelectedItemCategory(item, data))
                }, { error ->
                    Timber.d("[todo] 할 일 상세조회 실패 -> $error")
                })
            }
        }
    }

    private fun setSelectedItemCategory(item: TodoItemModel, response: TodoDetailItemModel): TodoItemModel {
        val categoryId: Long =
            uiState.value.categoryList.firstOrNull { it.categoryName == response.categoryName && it.categoryImgUrl == response.emojiImageUrl }?.categoryId ?: -1
        val todoItem: TodoItemModel = item.copy(categoryId = categoryId)

        updateState(
            uiState.value.copy(
                selectedItem = todoItem,
            )
        )

        return todoItem
    }

    fun updateCategory(todoId: Long, categoryId: Long?) {
        viewModelScope.launch {
            updateTodoCategoryUseCase(request = UpdateTodoCategoryModel(
                todoId = todoId,
                todoCategoryModel = TodoCategoryIdModel(categoryId)
            )
            ).collect {
                resultResponse(it, {
                }, { error ->
                    Timber.d("[카테고리] 수정 서버통신 실패 -> $error")
                })
            }
        }
    }
}