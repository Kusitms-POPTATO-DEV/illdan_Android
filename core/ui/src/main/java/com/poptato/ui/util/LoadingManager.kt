package com.poptato.ui.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LoadingManager {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun startLoading() {
        _isLoading.value = true
    }

    fun endLoading() {
        _isLoading.value = false
    }
}