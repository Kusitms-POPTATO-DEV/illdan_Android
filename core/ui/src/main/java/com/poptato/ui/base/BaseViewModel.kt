package com.poptato.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poptato.ui.util.LoadingManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<STATE: PageState>(
    initialState: STATE
): ViewModel() {
    protected val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    protected fun updateState(state: STATE) {
        viewModelScope.launch {
            _uiState.update { state }
        }
    }

    protected fun emitEventFlow(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    protected fun<D> resultResponse(
        response: Result<D>,
        successCallback: (D) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
        needLoading: Boolean = false
    ) {
        if (needLoading) LoadingManager.startLoading()

        viewModelScope.launch {
            response.fold(
                onSuccess = { data ->
                    successCallback.invoke(data)
                },
                onFailure = { throwable ->
                    Timber.e("에러 발생", throwable.stackTraceToString())
                    errorCallback?.invoke(throwable)
                }
            )

            if (needLoading) LoadingManager.endLoading()
        }
    }
}