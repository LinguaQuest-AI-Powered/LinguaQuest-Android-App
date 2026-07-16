package com.iti.linguaquest.features.game.presentation.proccessing.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.features.game.presentation.proccessing.contract.ProcessingEffect
import com.iti.linguaquest.features.game.presentation.proccessing.contract.ProcessingIntent
import com.iti.linguaquest.features.game.presentation.proccessing.contract.ProcessingState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProcessingViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ProcessingState())
    val state: StateFlow<ProcessingState> = _state.asStateFlow()

    private val _effect = Channel<ProcessingEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: ProcessingIntent) {
        when (intent) {
            ProcessingIntent.SimulateAiSuccess -> sendEffect(ProcessingEffect.NavigateToSuccess)
            ProcessingIntent.SimulateAiFailure -> sendEffect(ProcessingEffect.NavigateToFailure)
        }
    }

    private fun sendEffect(effect: ProcessingEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}