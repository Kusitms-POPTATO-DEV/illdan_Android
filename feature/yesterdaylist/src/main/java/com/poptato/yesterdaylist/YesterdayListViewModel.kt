package com.poptato.yesterdaylist

import androidx.lifecycle.viewModelScope
import com.poptato.domain.model.enums.TodoStatus
import com.poptato.domain.model.request.ListRequestModel
import com.poptato.domain.model.request.todo.TodoIdsModel
import com.poptato.domain.model.response.yesterday.YesterdayItemModel
import com.poptato.domain.model.response.yesterday.YesterdayListModel
import com.poptato.domain.usecase.todo.UpdateTodoCompletionUseCase
import com.poptato.domain.usecase.yesterday.GetYesterdayListUseCase
import com.poptato.domain.usecase.yesterday.UpdateYesterdayTodoCompletionUseCase
import com.poptato.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class YesterdayListViewModel @Inject constructor(
    private val getYesterdayListUseCase: GetYesterdayListUseCase,
    private val updateTodoCompletionUseCase: UpdateTodoCompletionUseCase,
    private val updateYesterdayTodoCompletionUseCase: UpdateYesterdayTodoCompletionUseCase
): BaseViewModel<YesterdayListPageState>(
    YesterdayListPageState()
) {

    init {
        getYesterdayList(0, 100)
    }

    private fun getYesterdayList(page: Int, size: Int) {
        viewModelScope.launch {
            getYesterdayListUseCase(request = ListRequestModel(page = page, size = size)).collect {
                resultResponse(it, { data ->
                    setMappingToYesterdayList(data)
                    Timber.d("[어제 한 일] 서버통신 성공 -> $data")
                }, { error ->
                    Timber.d("[어제 한 일] 서버통신 실패 -> $error")
                })
            }
        }
    }

    private fun setMappingToYesterdayList(response: YesterdayListModel) {
        updateState(
            uiState.value.copy(
                yesterdayList = response.yesterdays,
                totalPageCount = response.totalPageCount
            )
        )
    }

    fun onCheckedTodo(id: Long, status: TodoStatus) {
        val newStatus = if (status == TodoStatus.COMPLETED) TodoStatus.INCOMPLETE else TodoStatus.COMPLETED
        val updatedList = uiState.value.yesterdayList.map { item ->
            if (item.todoId == id) {
                item.copy(todoStatus = newStatus)
            } else {
                item
            }
        }
        val completedList = uiState.value.completedTodoList.toMutableList()

        if (newStatus == TodoStatus.COMPLETED) { completedList.add(id) }
        else { completedList.remove(id) }

        updateState(
            uiState.value.copy(
                yesterdayList = updatedList,
                completedTodoList = completedList
            )
        )
    }

    fun updateYesterdayTodoCompletion() {
        viewModelScope.launch {
            updateYesterdayTodoCompletionUseCase(
                request = TodoIdsModel(
                    todoIds = uiState.value.completedTodoList.map { it }
                )
            ).collect {
                resultResponse(it, {})
            }
        }
    }
}