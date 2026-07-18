package com.iti.linguaquest.features.game.presentation.processing.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingEffect
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingIntent
import com.iti.linguaquest.features.game.presentation.processing.contract.GameProcessingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameProcessingViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GameProcessingState())
    val state: StateFlow<GameProcessingState> = _state.asStateFlow()

    private val _effect = Channel<GameProcessingEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: GameProcessingIntent) {
        when (intent) {
            GameProcessingIntent.StartProcessing -> {
                // Future implementation: Hit the AI API
            }
            GameProcessingIntent.SimulateAiSuccess -> {
                sendEffect(GameProcessingEffect.NavigateToSuccess(50, 10))
            }
            GameProcessingIntent.SimulateAiFailure -> {
                sendEffect(GameProcessingEffect.NavigateToFailure("That looks like a shoe, not an apple!"))
            }
            GameProcessingIntent.SimulateNetworkError -> {
                sendEffect(GameProcessingEffect.NavigateToError("Connection timed out."))
            }
        }
    }

    private fun sendEffect(effect: GameProcessingEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}