package com.iti.linguaquest.features.game.presentation.result.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultEffect
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultIntent
import com.iti.linguaquest.features.game.presentation.result.contract.GameResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameResultViewModel @Inject constructor() : ViewModel() {

    // Initialized with a fallback state.
    // We will populate this with the actual result from the ProcessingScreen via navigation arguments.
    private val _state = MutableStateFlow<GameResultUiState>(GameResultUiState.Error(com.iti.linguaquest.core.sharedComponents.text.UiText.DynamicString("Loading result...")))
    val state: StateFlow<GameResultUiState> = _state.asStateFlow()

    private val _effect = Channel<GameResultEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: GameResultIntent) {
        when (intent) {
            // User Action Intents
            GameResultIntent.RetryClicked -> sendEffect(GameResultEffect.NavigateToCamera)
            GameResultIntent.BuyHintClicked -> sendEffect(GameResultEffect.ApplyHintAndRetry)
            GameResultIntent.NextLevelClicked -> sendEffect(GameResultEffect.NavigateToNextLevel)
            GameResultIntent.ExitClicked -> sendEffect(GameResultEffect.NavigateToExit)
        }
    }

    // Call this from the screen or NavHost to inject the outcome
    fun setInitialResult(resultState: GameResultUiState) {
        _state.value = resultState
    }

    private fun sendEffect(effect: GameResultEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}