package com.iti.linguaquest.features.game.presentation.failure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.failure.contract.GameFailureEffect
import com.iti.linguaquest.features.game.presentation.failure.contract.GameFailureIntent
import com.iti.linguaquest.features.game.presentation.failure.contract.GameFailureState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameFailureViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(GameFailureState())
    val state: StateFlow<GameFailureState> = _state.asStateFlow()

    private val _effect = Channel<GameFailureEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: GameFailureIntent) {
        when (intent) {
            GameFailureIntent.RetryClicked -> sendEffect(GameFailureEffect.NavigateToRetry)
            GameFailureIntent.BuyHintClicked -> sendEffect(GameFailureEffect.ApplyHint)
            GameFailureIntent.ExitClicked -> sendEffect(GameFailureEffect.NavigateToExit)
        }
    }

    private fun sendEffect(effect: GameFailureEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}