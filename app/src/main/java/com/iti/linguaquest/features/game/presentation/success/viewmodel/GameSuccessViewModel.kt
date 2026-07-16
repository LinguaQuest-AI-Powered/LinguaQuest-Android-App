package com.iti.linguaquest.features.game.presentation.success.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.success.contract.GameSuccessEffect
import com.iti.linguaquest.features.game.presentation.success.contract.GameSuccessIntent
import com.iti.linguaquest.features.game.presentation.success.contract.GameSuccessState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SuccessViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(GameSuccessState())
    val state: StateFlow<GameSuccessState> = _state.asStateFlow()

    private val _effect = Channel<GameSuccessEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: GameSuccessIntent) {
        when (intent) {
            GameSuccessIntent.NextLevelClicked -> sendEffect(GameSuccessEffect.NavigateToNextLevel)
            GameSuccessIntent.ExitClicked -> sendEffect(GameSuccessEffect.NavigateToExit)
        }
    }

    private fun sendEffect(effect: GameSuccessEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}