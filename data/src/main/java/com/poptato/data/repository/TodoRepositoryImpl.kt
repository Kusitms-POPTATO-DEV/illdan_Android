package com.poptato.data.repository

import com.poptato.data.base.BaseRepository
import com.poptato.data.mapper.TodoDetailResponseMapper
import com.poptato.data.mapper.UnitResponseMapper
import com.poptato.data.service.TodoService
import com.poptato.domain.model.request.todo.DragDropRequestModel
import com.poptato.domain.model.request.todo.ModifyTodoRequestModel
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.model.request.todo.UpdateDeadlineRequestModel
import com.poptato.domain.model.request.todo.UpdateTodoCategoryModel
import com.poptato.domain.model.response.todo.TodoDetailItemModel
import com.poptato.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoService: TodoService
) : TodoRepository, BaseRepository() {
    override suspend fun deleteTodo(todoId: Long): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { todoService.deleteTodo(todoId) }, UnitResponseMapper)
    }

    override suspend fun modifyTodo(request: ModifyTodoRequestModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = {
            todoService.modifyTodo(
                todoId = request.todoId,
                request = request.content
            )
        }, UnitResponseMapper)
    }

    override suspend fun dragDrop(request: DragDropRequestModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { todoService.dragDrop(request) }, UnitResponseMapper)
    }

    override suspend fun updateDeadline(request: UpdateDeadlineRequestModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = {
            todoService.updateDeadline(
                todoId = request.todoId,
                request = request.deadline
            )
        }, UnitResponseMapper)
    }

    override suspend fun updateBookmark(todoId: Long): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { todoService.updateBookmark(todoId) }, UnitResponseMapper)
    }

    override suspend fun swipeTodo(request: TodoIdModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { todoService.swipeTodo(request) }, UnitResponseMapper)
    }

    override suspend fun updateTodoCompletion(todoId: Long): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { todoService.updateTodoCompletion(todoId) }, UnitResponseMapper)
    }

    override suspend fun updateTodoCategory(request: UpdateTodoCategoryModel): Flow<Result<Unit>> {
        return apiLaunch(apiCall = {
            todoService.updateTodoCategory(
                todoId = request.todoId,
                request = request.todoCategoryModel
            )
        }, UnitResponseMapper)
    }

    override suspend fun getTodoDetail(todoId: Long): Flow<Result<TodoDetailItemModel>> {
        return apiLaunch(apiCall = {
            todoService.getTodoDetail(todoId = todoId)
        }, TodoDetailResponseMapper)
    }

    override suspend fun updateTodoRepeat(todoId: Long): Flow<Result<Unit>> {
        return apiLaunch(apiCall = { todoService.updateTodoRepeat(todoId) }, UnitResponseMapper)
    }
}