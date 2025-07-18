package com.poptato.domain.repository

import com.poptato.domain.model.request.todo.DragDropRequestModel
import com.poptato.domain.model.request.todo.ModifyTodoRequestModel
import com.poptato.domain.model.request.todo.RoutineRequestModel
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.model.request.todo.TodoTimeModel
import com.poptato.domain.model.request.todo.UpdateDeadlineRequestModel
import com.poptato.domain.model.request.todo.UpdateTodoCategoryModel
import com.poptato.domain.model.response.todo.TodoDetailItemModel
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun deleteTodo(todoId: Long): Flow<Result<Unit>>
    suspend fun modifyTodo(request: ModifyTodoRequestModel): Flow<Result<Unit>>
    suspend fun dragDrop(request: DragDropRequestModel): Flow<Result<Unit>>
    suspend fun updateDeadline(request: UpdateDeadlineRequestModel): Flow<Result<Unit>>
    suspend fun updateBookmark(todoId: Long): Flow<Result<Unit>>
    suspend fun swipeTodo(request: TodoIdModel): Flow<Result<Unit>>
    suspend fun updateTodoCompletion(todoId: Long): Flow<Result<Unit>>
    suspend fun updateTodoCategory(request: UpdateTodoCategoryModel): Flow<Result<Unit>>
    suspend fun getTodoDetail(todoId: Long): Flow<Result<TodoDetailItemModel>>
    suspend fun setTodoRepeat(todoId: Long): Flow<Result<Unit>>
    suspend fun deleteTodoRepeat(todoId: Long): Flow<Result<Unit>>
    suspend fun updateTodoTime(todoId: Long, request: TodoTimeModel): Flow<Result<Unit>>
    suspend fun setTodoRoutine(todoId: Long, request: RoutineRequestModel): Flow<Result<Unit>>
    suspend fun deleteTodoRoutine(todoId: Long): Flow<Result<Unit>>
}