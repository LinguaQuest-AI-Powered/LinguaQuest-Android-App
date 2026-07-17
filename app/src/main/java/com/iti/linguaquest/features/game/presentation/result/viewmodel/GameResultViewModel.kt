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

    private val _state = MutableStateFlow<GameResultUiState>(GameResultUiState.Processing)
    val state: StateFlow<GameResultUiState> = _state.asStateFlow()

    private val _effect = Channel<GameResultEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: GameResultIntent) {
        when (intent) {
            // State Transitions (Simulating AI response)
            GameResultIntent.SimulateAiSuccess -> {
                _state.value = GameResultUiState.Success(xpAwarded = 50, coinsAwarded = 10)
            }
            GameResultIntent.SimulateAiFailure -> {
                _state.value = GameResultUiState.Failure(reason = "That looks like a shoe, not an apple!")
            }
            GameResultIntent.SimulateNetworkError -> {
                _state.value = GameResultUiState.Error(errorMessage = "Connection timed out.")
            }

            // Navigation Effects
            GameResultIntent.RetryClicked -> sendEffect(GameResultEffect.NavigateToCamera)
            GameResultIntent.BuyHintClicked -> sendEffect(GameResultEffect.ApplyHintAndRetry)
            GameResultIntent.NextLevelClicked -> sendEffect(GameResultEffect.NavigateToNextLevel)
            GameResultIntent.ExitClicked -> sendEffect(GameResultEffect.NavigateToExit)
        }
    }

    private fun sendEffect(effect: GameResultEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}