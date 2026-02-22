package com.poptato.ui.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object CommonEventManager {
    private val _logoutTriggerFlow: MutableSharedFlow<Unit> = MutableSharedFlow()
    val logoutTriggerFlow = _logoutTriggerFlow.asSharedFlow()

    suspend fun triggerLogout() {
        _logoutTriggerFlow.emit(Unit)
    }
}