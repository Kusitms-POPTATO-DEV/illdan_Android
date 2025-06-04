package com.poptato.backlog

import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.poptato.domain.model.enums.TodoType
import com.poptato.core.util.DateTimeFormatter
import com.poptato.core.util.move
import com.poptato.domain.model.request.backlog.CreateBacklogRequestModel
import com.poptato.domain.model.request.backlog.GetBacklogListRequestModel
import com.poptato.domain.model.request.category.CategoryDragDropRequestModel
import com.poptato.domain.model.request.category.GetCategoryListRequestModel
import com.poptato.domain.model.request.todo.DeadlineContentModel
import com.poptato.domain.model.request.todo.DragDropRequestModel
import com.poptato.domain.model.request.todo.ModifyTodoRequestModel
import com.poptato.domain.model.request.todo.RoutineRequestModel
import com.poptato.domain.model.request.todo.TodoCategoryIdModel
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.model.request.todo.TodoTimeModel
import com.poptato.domain.model.request.todo.UpdateDeadlineRequestModel
import com.poptato.domain.model.request.todo.UpdateTodoCategoryModel
import com.poptato.domain.model.response.backlog.BacklogListModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryListModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.domain.model.response.todo.TodoDetailItemModel
import com.poptato.domain.usecase.auth.GetDeadlineDateModeUseCase
import com.poptato.domain.usecase.backlog.CreateBacklogUseCase
import com.poptato.domain.usecase.backlog.GetBacklogListUseCase
import com.poptato.domain.usecase.category.CategoryDragDropUseCase
import com.poptato.domain.usecase.category.DeleteCategoryUseCase
import com.poptato.domain.usecase.category.GetCategoryListUseCase
import com.poptato.domain.usecase.todo.DeleteTodoUseCase
import com.poptato.domain.usecase.todo.DragDropUseCase
import com.poptato.domain.usecase.todo.GetTodoDetailUseCase
import com.poptato.domain.usecase.todo.ModifyTodoUseCase
import com.poptato.domain.usecase.todo.SwipeTodoUseCase
import com.poptato.domain.usecase.todo.UpdateBookmarkUseCase
import com.poptato.domain.usecase.todo.UpdateDeadlineUseCase
import com.poptato.domain.usecase.todo.UpdateRoutineUseCase
import com.poptato.domain.usecase.todo.UpdateTodoRepeatUseCase
import com.poptato.domain.usecase.todo.UpdateTodoCategoryUseCase
import com.poptato.domain.usecase.todo.UpdateTodoTimeUseCase
import com.poptato.ui.base.BaseViewModel
import com.poptato.ui.event.BacklogExternalEvent
import com.poptato.ui.util.AnalyticsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.annotations.TestOnly
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class BacklogViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val createBacklogUseCase: CreateBacklogUseCase,
    private val getBacklogListUseCase: GetBacklogListUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val modifyTodoUseCase: ModifyTodoUseCase,
    private val dragDropUseCase: DragDropUseCase,
    private val updateDeadlineUseCase: UpdateDeadlineUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase,
    private val updateTodoCategoryUseCase: UpdateTodoCategoryUseCase,
    private val getTodoDetailUseCase: GetTodoDetailUseCase,
    private val swipeTodoUseCase: SwipeTodoUseCase,
    private val updateTodoRepeatUseCase: UpdateTodoRepeatUseCase,
    private val updateTodoTimeUseCase: UpdateTodoTimeUseCase,
    private val categoryDragDropUseCase: CategoryDragDropUseCase,
    private val getDeadlineDateModeUseCase: GetDeadlineDateModeUseCase,
    private val updateRoutineUseCase: UpdateRoutineUseCase
) : BaseViewModel<BacklogPageState>(
    BacklogPageState()
) {
    private val mutex = Mutex()
    private var snapshotList: List<TodoItemModel> = emptyList()
    @TestOnly
    @VisibleForTesting
    internal var tempTodoId: Long? = null

    init {
        getDeadlineDateMode()
        getBacklogList(-1, 0, 100)
    }

    fun getCategoryList(initialCategoryIndex: Int = 0) {
        viewModelScope.launch {
            getCategoryListUseCase(request = GetCategoryListRequestModel(0, 100)).collect {
                resultResponse(it, { data ->
                    onSuccessGetCategoryList(data, initialCategoryIndex)
                })
            }
        }
    }

    private fun onSuccessGetCategoryList(response: CategoryListModel, initialCategoryIndex: Int = 0) {
        if (initialCategoryIndex != 0) getBacklogListInCategory(initialCategoryIndex, response.categoryList)
        updateStateSync(
            uiState.value.copy(
                categoryList = response.categoryList
            )
        )
    }

    fun deleteCategory() {
        AnalyticsManager.logEvent(
            eventName = "delete_category",
            params = mapOf(
                "category_name" to uiState.value.categoryList[uiState.value.selectedCategoryIndex].categoryName,
                "delete_date" to DateTimeFormatter.getTodayFullDate()
            )
        )

        viewModelScope.launch {
            deleteCategoryUseCase(request = uiState.value.selectedCategoryId).collect {
                resultResponse(it, {
                    getCategoryList()
                    updateState(uiState.value.copy(selectedCategoryId = -1, selectedCategoryIndex = 0))
                    getBacklogList(-1, 0, 100)
                }, { error ->
                    Timber.d("[카테고리] 삭제 서버통신 실패 -> $error")
                })
            }
        }
    }

    fun getBacklogListInCategory(categoryIndex: Int, categoryList: List<CategoryItemModel> = uiState.value.categoryList) {
        updateSelectedCategory(categoryIndex, categoryList)
        getBacklogList(uiState.value.selectedCategoryId, 0, 100)
    }

    private fun updateSelectedCategory(categoryIndex: Int, categoryList: List<CategoryItemModel> = uiState.value.categoryList) {
        updateStateSync(
            uiState.value.copy(
                selectedCategoryIndex = categoryIndex,
                selectedCategoryId = categoryList[categoryIndex].categoryId
            )
        )

        AnalyticsManager.logEvent(
            eventName = "view_category",
            params = mapOf("category_name" to categoryList[categoryIndex].categoryName)
        )
    }

    private fun getBacklogList(categoryId: Long, page: Int, size: Int) {
        viewModelScope.launch {
            getBacklogListUseCase(request = GetBacklogListRequestModel(categoryId = categoryId, page = page, size = size)).collect {
                resultResponse(it, ::onSuccessGetBacklogList)
            }
        }

        AnalyticsManager.logEvent(
            eventName = "get_backlog",
            params = mapOf("button_name" to "할 일 내비게이션바 버튼", "user_action" to "백로그 전체 조회")
        )
    }

    private fun onSuccessGetBacklogList(response: BacklogListModel) {
        viewModelScope.launch { updateSnapshotList(response.backlogs) }

        updateStateSync(
            uiState.value.copy(
                backlogList = response.backlogs,
                totalPageCount = response.totalPageCount,
                totalItemCount = response.totalCount,
                isFinishedInitialization = true
            )
        )
    }

    // -------- 스냅샷을 업데이트하기 위한 메서드 --------
    private fun getBacklogListForSnapshot() {
        viewModelScope.launch {
            getBacklogListUseCase(request = GetBacklogListRequestModel(categoryId = -1, page = 0, size = 100)).collect {
                resultResponse(it, ::onSuccessUpdateSnapshot)
            }
        }
    }

    private fun onSuccessUpdateSnapshot(response: BacklogListModel) {
        viewModelScope.launch {
            updateSnapshotList(response.backlogs)
        }
    }

    fun createBacklog(content: String) {
        if (content.isBlank()) return

        addTemporaryBacklog(content)

        viewModelScope.launch {
            createBacklogUseCase(
                request = CreateBacklogRequestModel(uiState.value.selectedCategoryId, content)
            ).collect {
                resultResponse(it, ::onSuccessCreateBacklog, { onFailedUpdateBacklogList() })
            }
        }
    }

    @TestOnly
    @VisibleForTesting
    internal fun addTemporaryBacklog(content: String) {
        val newList = uiState.value.backlogList.toMutableList()
        val temporaryId = Random.nextLong()
        val currentCategory = uiState.value.categoryList[uiState.value.selectedCategoryIndex]
        val newItem = TodoItemModel(
            todoId = temporaryId,
            content = content,
            isBookmark = uiState.value.selectedCategoryIndex == 1,
            categoryName = currentCategory.categoryName,
            categoryId = currentCategory.categoryId,
            imageUrl = currentCategory.categoryImgUrl
        )

        newList.add(0, newItem)
        updateNewItemFlag(true)
        updateList(newList)
        tempTodoId = temporaryId
    }

    private fun onSuccessCreateBacklog(response: TodoIdModel) {
        AnalyticsManager.logEvent(
            eventName = "make_task",
            params = mapOf(
                "make_date" to DateTimeFormatter.getToday(),
                "task_ID" to response.todoId
            )
        )
        val updatedList = uiState.value.backlogList.map { item ->
            if (item.todoId == tempTodoId) {
                item.copy(todoId = response.todoId)
            } else item
        }

        updateList(updatedList)
        getBacklogListForSnapshot()
    }

    private fun onFailedUpdateBacklogList() {
        viewModelScope.launch {
            val snapshot = getSnapshotList()
            updateList(snapshot)
            emitEventFlow(BacklogEvent.OnFailedUpdateBacklogList)
        }
    }

    fun swipeBacklogItem(item: TodoItemModel) {
        AnalyticsManager.logEvent(
            eventName = "add_today",
            params = mapOf("add_date" to DateTimeFormatter.getTodayFullDate(), "task_ID" to "${item.todoId}")
        )

        val newList = uiState.value.backlogList.filter { it.todoId != item.todoId }

        updateList(newList)

        viewModelScope.launch {
            swipeTodoUseCase(TodoIdModel(item.todoId)).collect {
                resultResponse(
                    it,
                    { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) }  },
                    { onFailedUpdateBacklogList() }
                )
            }
        }
    }

    fun onMove(from: Int, to: Int) {
        val currentList = uiState.value.backlogList.toMutableList()
        currentList.move(from, to)
        updateList(currentList)
    }

    fun onMoveCategory(from: Int, to: Int) {
        if (from == 0 || from == 1 || to == 0 || to == 1) return

        val currentList = uiState.value.categoryList.toMutableList()
        currentList.move(from, to)
        updateCategoryList(currentList)
    }

    fun onDragEnd() {
        val todoIdList = uiState.value.backlogList.map { it.todoId }

        viewModelScope.launch {
            dragDropUseCase.invoke(
                request = DragDropRequestModel(
                    type = TodoType.BACKLOG,
                    todoIds = todoIdList
                )
            ).collect {
                resultResponse(
                    it,
                    { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) } },
                    { onFailedUpdateBacklogList() }
                )
            }
        }
    }

    fun onCategoryDragEnd() {
        val categoryIds = uiState.value.categoryList.map { it.categoryId }.toMutableList()

        categoryIds.removeAt(0)
        categoryIds.removeAt(0)

        viewModelScope.launch {
            categoryDragDropUseCase(CategoryDragDropRequestModel(categoryIds)).collect {
                resultResponse(it, {})
            }
        }
    }

    @TestOnly
    @VisibleForTesting
    internal fun updateList(updatedList: List<TodoItemModel>) {
        val newList = updatedList.toList()
        updateState(
            uiState.value.copy(
                backlogList = newList
            )
        )
    }

    private fun updateCategoryList(newList: List<CategoryItemModel>) {
        updateState(
            uiState.value.copy(categoryList = newList)
        )
    }

    fun updateCategory(todoId: Long, categoryId: Long?) {
        Timber.d("[수정 테스트] ${uiState.value.backlogList} -> $todoId $categoryId")

        viewModelScope.launch {
            updateTodoCategoryUseCase(request = UpdateTodoCategoryModel(
                todoId = todoId,
                todoCategoryModel = TodoCategoryIdModel(categoryId)
            )).collect {
                resultResponse(it, {
                    getBacklogList(categoryId = uiState.value.selectedCategoryId, page = 0, size = uiState.value.backlogList.size)
                }, { error ->
                    Timber.d("[카테고리] 수정 서버통신 실패 -> $error")
                })
            }
        }
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
                resultResponse(it, { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) } }, { onFailedUpdateBacklogList() })
            }
        }
    }

    private fun updateDeadlineInUI(deadline: String?, id: Long) {
        val dDay = DateTimeFormatter.calculateDDay(deadline)
        val newList = uiState.value.backlogList.map {
            if (it.todoId == id) {
                it.copy(deadline = deadline ?: "", dDay = dDay)
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(deadline = deadline ?: "", dDay = dDay)

        updateState(
            uiState.value.copy(
                backlogList = newList,
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

    fun deleteBacklog(id: Long) {
        val newList = uiState.value.backlogList.filter { it.todoId != id }
        updateList(newList)

        viewModelScope.launch {
            deleteTodoUseCase.invoke(id).collect {
                resultResponse(
                    it,
                    {
                        viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) }
                        emitEventFlow(BacklogEvent.OnSuccessDeleteBacklog)
                    },
                    { onFailedUpdateBacklogList() }
                )
            }
        }
    }

    fun modifyTodo(item: ModifyTodoRequestModel) {
        modifyTodoInUI(item = item)

        viewModelScope.launch {
            modifyTodoUseCase.invoke(
                request = item
            ).collect {
                resultResponse(it, { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) } }, { onFailedUpdateBacklogList() })
            }
        }
    }

    private fun modifyTodoInUI(item: ModifyTodoRequestModel) {
        val newList = uiState.value.backlogList.map {
            if (it.todoId == item.todoId) {
                it.copy(content = item.content.content)
            } else {
                it
            }
        }

        updateList(newList)
    }

    fun updateNewItemFlag(flag: Boolean) {
        updateState(
            uiState.value.copy(
                isNewItemCreated = flag
            )
        )
    }

    fun updateBookmark(id: Long) {
        updateBookmarkInUI(id)

        viewModelScope.launch {
            updateBookmarkUseCase.invoke(id).collect {
                resultResponse(it, { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) } }, { onFailedUpdateBacklogList() })
            }
        }
    }

    private fun updateBookmarkInUI(id: Long) {
        val newList = uiState.value.backlogList.map {
            if (it.todoId == id) {
                it.copy(isBookmark = !it.isBookmark)
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(isBookmark = !uiState.value.selectedItem.isBookmark)

        updateState(
            uiState.value.copy(
                backlogList = newList,
                selectedItem = updatedItem
            )
        )
    }

    private fun toggleRepeat(id: Long) {
        updateTodoRepeatInUI(id)

        viewModelScope.launch {
            updateTodoRepeatUseCase(id).collect {
                resultResponse(it, { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) } }, { onFailedUpdateBacklogList() })
            }
        }
    }

    private fun updateTodoRepeatInUI(id: Long) {
        val newList = uiState.value.backlogList.map {
            if (it.todoId == id) {
                it.copy(isRepeat = !it.isRepeat)
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(isRepeat = !uiState.value.selectedItem.isRepeat)

        updateState(
            uiState.value.copy(
                backlogList = newList,
                selectedItem = updatedItem
            )
        )
    }

    fun updateTodoTime(id: Long, time: String) {
        updateTodoTimeInUI(id, time)

        viewModelScope.launch {
            updateTodoTimeUseCase(request = UpdateTodoTimeUseCase.Companion.UpdateTodoTimeModel(
                todoId = id,
                requestModel = TodoTimeModel(todoTime = time)
            )).collect {
                resultResponse(it, { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) } }, { onFailedUpdateBacklogList() })
            }
        }
    }

    private fun updateTodoTimeInUI(id: Long, time: String) {
        val newList = uiState.value.backlogList.map {
            if (it.todoId == id) {
                it.copy(time = time)
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(time = time)

        updateState(
            uiState.value.copy(
                backlogList = newList,
                selectedItem = updatedItem
            )
        )
    }

    fun updateRoutine(id: Long, activeIndex: List<Int>?) {
        val request = RoutineRequestModel()
        request.convertIndexToDays(activeIndex)

        updateRoutineInUI(id, request.routineDays)

        viewModelScope.launch {
            updateRoutineUseCase(request = UpdateRoutineUseCase.Companion.UpdateTodoRoutineModel(id, request)).collect {
                resultResponse(it, { viewModelScope.launch { updateSnapshotList(uiState.value.backlogList) } }, { onFailedUpdateBacklogList() })
            }
        }
    }

    private fun updateRoutineInUI(id: Long, routineDays: List<String>?) {
        val newList = uiState.value.backlogList.map {
            if (it.todoId == id) {
                it.copy(routineDays = routineDays ?: emptyList())
            } else {
                it
            }
        }
        val updatedItem = uiState.value.selectedItem.copy(routineDays = routineDays ?: emptyList())

        updateState(
            uiState.value.copy(
                backlogList = newList,
                selectedItem = updatedItem
            )
        )
    }

    @TestOnly
    @VisibleForTesting
    internal suspend fun updateSnapshotList(newList: List<TodoItemModel>) {
        mutex.withLock {
            snapshotList = newList
        }
    }

    private suspend fun getSnapshotList(): List<TodoItemModel> {
        return mutex.withLock {
            snapshotList
        }
    }

    // DeadlineDateMode
    private fun getDeadlineDateMode() {
        viewModelScope.launch {
            getDeadlineDateModeUseCase(Unit).collect {
                updateState(uiState.value.copy(isDeadlineDateMode = it))
            }
        }
    }

    // 할 일 수정 텍스트 필드 관련 메서드
    fun updateActiveItemId(id: Long?) {
        updateState(uiState.value.copy(activeItemId = id))
    }

    // 외부 Flow 이벤트를 처리하는 메서드
    fun observeExternalEvents(events: SharedFlow<BacklogExternalEvent>) {
        viewModelScope.launch {
            events.collect { event ->
                when(event) {
                    is BacklogExternalEvent.ActiveItem -> { updateActiveItemId(event.id) }
                    is BacklogExternalEvent.DeleteTodo -> { deleteBacklog(event.id) }
                    is BacklogExternalEvent.ToggleRepeat -> { toggleRepeat(event.id) }
                    is BacklogExternalEvent.UpdateBookmark -> { updateBookmark(event.id) }
                    is BacklogExternalEvent.UpdateCategory -> { updateCategory(uiState.value.selectedItem.todoId, event.id) }
                    is BacklogExternalEvent.UpdateDeadline -> { setDeadline(event.deadline, uiState.value.selectedItem.todoId) }
                    is BacklogExternalEvent.UpdateTime -> { updateTodoTime(event.info.first, event.info.second) }
                }
            }
        }
    }
}