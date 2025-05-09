package com.poptato.ui.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(): ViewModel() {
    private val _isNewUser = MutableStateFlow(false)
    val isNewUser: StateFlow<Boolean> = _isNewUser

    private val _showFirstGuide = MutableStateFlow(false)
    val showFirstGuide: StateFlow<Boolean> = _showFirstGuide

    private val _showSecondGuide = MutableStateFlow(false)
    val showSecondGuide: StateFlow<Boolean> = _showSecondGuide

    fun updateIsNewUser(value: Boolean) {
        _isNewUser.value = value
    }

    fun updateFirstGuide(value: Boolean) {
        _showFirstGuide.value = value
    }

    fun updateSecondGuide(value: Boolean) {
        _showSecondGuide.value = value
    }
}