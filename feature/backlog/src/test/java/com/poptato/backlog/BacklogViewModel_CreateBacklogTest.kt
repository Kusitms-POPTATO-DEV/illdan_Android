package com.poptato.backlog

import com.poptato.core.util.DateTimeFormatter
import com.poptato.domain.model.request.todo.TodoIdModel
import com.poptato.domain.model.response.today.TodoItemModel
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
import com.poptato.domain.usecase.todo.UpdateTodoCategoryUseCase
import com.poptato.domain.usecase.todo.SetTodoRepeatUseCase
import com.poptato.domain.usecase.todo.UpdateTodoTimeUseCase
import com.poptato.ui.util.AnalyticsManager
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class BacklogViewModel_CreateBacklogTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: BacklogViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // AnalyticsManager 코드 무시
        mockkObject(AnalyticsManager)
        every { AnalyticsManager.logEvent(any(), any()) } just Runs
        // DateTimeFormatter 코드 무시
        mockkObject(DateTimeFormatter)
        every { DateTimeFormatter.getToday() } returns "2025-06-03"
    }

    @Test
    fun `임시 아이템이 먼저 리스트에 추가된다`() = runTest {
        // given
        val fakeFlow: Flow<Result<TodoIdModel>> = flowOf(Result.success(TodoIdModel(999L)))
        val mockCreate = mockk<CreateBacklogUseCase>()
        coEvery { mockCreate(any()) } returns fakeFlow
        viewModel = createViewModelWithFakeUseCase(mockCreate)

        // when
        val content = "Test 할일"
        viewModel.addTemporaryBacklog(content)
        advanceUntilIdle()

        // then
        val firstItem = viewModel.uiState.value.backlogList.first()
        assertEquals(content, firstItem.content)
        assertEquals(firstItem.todoId, viewModel.tempTodoId)
    }

    @Test
    fun `빈 문자열인 경우 아이템이 추가되지 않는다`() = runTest {
        // given
        val fakeFlow: Flow<Result<TodoIdModel>> = flowOf(Result.success(TodoIdModel(999L)))
        val mockCreate = mockk<CreateBacklogUseCase>()
        coEvery { mockCreate(any()) } returns fakeFlow
        viewModel = createViewModelWithFakeUseCase(mockCreate)

        // when
        val initialList = viewModel.uiState.value.backlogList
        viewModel.createBacklog("")
        advanceUntilIdle()

        // then
        val updatedList = viewModel.uiState.value.backlogList
        assertEquals(initialList, updatedList) // 변화가 없어야 함
    }

    @Test
    fun `공백만 입력된 경우 아이템이 추가되지 않는다`() = runTest {
        // given
        val fakeFlow: Flow<Result<TodoIdModel>> = flowOf(Result.success(TodoIdModel(999L)))
        val mockCreate = mockk<CreateBacklogUseCase>()
        coEvery { mockCreate(any()) } returns fakeFlow
        viewModel = createViewModelWithFakeUseCase(mockCreate)

        // when
        val initialList = viewModel.uiState.value.backlogList
        viewModel.createBacklog("     ")
        advanceUntilIdle()

        // then
        val updatedList = viewModel.uiState.value.backlogList
        assertEquals(initialList, updatedList)
    }

    @Test
    fun `서버 응답 성공 시 ID가 갱신된다`() = runTest {
        // given
        val fakeFlow: Flow<Result<TodoIdModel>> = flowOf(Result.success(TodoIdModel(999L)))
        val mockCreate = mockk<CreateBacklogUseCase>()
        coEvery { mockCreate(any()) } returns fakeFlow
        viewModel = createViewModelWithFakeUseCase(mockCreate)

        // when
        val content = "Test 성공"
        viewModel.createBacklog(content)
        advanceUntilIdle()

        // then
        val firstItem = viewModel.uiState.value.backlogList.first()
        assertEquals(content, firstItem.content)
        assertEquals(999L, firstItem.todoId)
    }

    @Test
    fun `서버 응답 실패 시 snapshotList로 복원된다`() = runTest {
        val fakeFlow: Flow<Result<TodoIdModel>> = flowOf(Result.failure(Exception("서버 응답 실패")))
        val mockCreate = mockk<CreateBacklogUseCase>()
        coEvery { mockCreate(any()) } returns fakeFlow
        viewModel = createViewModelWithFakeUseCase(mockCreate)

        // given
        val initialList = listOf(TodoItemModel(content = "기존", todoId = 1L))
        viewModel.updateList(initialList)
        viewModel.updateSnapshotList(initialList)
        advanceUntilIdle()
        assertEquals(initialList, viewModel.uiState.value.backlogList)

        // when
        viewModel.createBacklog("실패할 내용")
        advanceUntilIdle()

        // then
        val currentList = viewModel.uiState.value.backlogList
        assertEquals(initialList, currentList)
    }

    private fun createViewModelWithFakeUseCase(fakeUseCase: CreateBacklogUseCase): BacklogViewModel {
        return BacklogViewModel(
            getCategoryListUseCase = mockk<GetCategoryListUseCase>(relaxed = true),
            deleteCategoryUseCase = mockk<DeleteCategoryUseCase>(relaxed = true),
            createBacklogUseCase = fakeUseCase,
            getBacklogListUseCase = mockk<GetBacklogListUseCase>(relaxed = true),
            deleteTodoUseCase = mockk<DeleteTodoUseCase>(relaxed = true),
            modifyTodoUseCase = mockk<ModifyTodoUseCase>(relaxed = true),
            dragDropUseCase = mockk<DragDropUseCase>(relaxed = true),
            updateDeadlineUseCase = mockk<UpdateDeadlineUseCase>(relaxed = true),
            updateBookmarkUseCase = mockk<UpdateBookmarkUseCase>(relaxed = true),
            updateTodoCategoryUseCase = mockk<UpdateTodoCategoryUseCase>(relaxed = true),
            getTodoDetailUseCase = mockk<GetTodoDetailUseCase>(relaxed = true),
            swipeTodoUseCase = mockk<SwipeTodoUseCase>(relaxed = true),
            setTodoRepeatUseCase = mockk<SetTodoRepeatUseCase>(relaxed = true),
            updateTodoTimeUseCase = mockk<UpdateTodoTimeUseCase>(relaxed = true),
            categoryDragDropUseCase = mockk<CategoryDragDropUseCase>(relaxed = true),
            getDeadlineDateModeUseCase = mockk<GetDeadlineDateModeUseCase>(relaxed = true)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}