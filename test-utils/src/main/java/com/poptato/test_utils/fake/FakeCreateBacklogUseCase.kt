package com.poptato.test_utils.fake

import com.poptato.domain.model.request.backlog.CreateBacklogRequestModel
import com.poptato.domain.model.request.todo.TodoIdModel
import kotlinx.coroutines.flow.Flow

class FakeCreateBacklogUseCase(
    result: Flow<Result<TodoIdModel>>
) : FakeUseCase<CreateBacklogRequestModel, Result<TodoIdModel>>(result)