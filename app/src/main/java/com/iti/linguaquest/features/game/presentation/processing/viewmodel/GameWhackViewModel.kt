package com.iti.linguaquest.features.game.presentation.processing.viewmodel

import androidx.lifecycle.ViewModel
import com.iti.linguaquest.features.game.presentation.processing.contract.GameWhackIntent
import com.iti.linguaquest.features.game.presentation.processing.contract.GameWhackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameWhackViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GameWhackState())
    val state: StateFlow<GameWhackState> = _state.asStateFlow()

    fun onIntent(intent: GameWhackIntent) {
        when (intent) {
            GameWhackIntent.StartGame -> {
                _state.update { it.copy(isGameActive = true) }
            }
            GameWhackIntent.LingoWhacked -> {
                _state.update { it.copy(currentCoins = it.currentCoins + 1) }
            }
            GameWhackIntent.StopGame -> {
                _state.update { it.copy(isGameActive = false) }
            }
        }
    }
}