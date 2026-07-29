package com.iti.linguaquest.features.help.presentation.guide.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.help.presentation.guide.contract.UserGuideEffect
import com.iti.linguaquest.features.help.presentation.guide.contract.UserGuideIntent
import com.iti.linguaquest.features.help.presentation.guide.contract.UserGuideState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserGuideViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(UserGuideState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<UserGuideEffect>()
    val effect: SharedFlow<UserGuideEffect> = _effect.asSharedFlow()

    fun onIntent(intent: UserGuideIntent) {
        when (intent) {
            UserGuideIntent.OnBackClicked -> emitEffect(UserGuideEffect.NavigateBack)
        }
    }

    private fun emitEffect(effect: UserGuideEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
