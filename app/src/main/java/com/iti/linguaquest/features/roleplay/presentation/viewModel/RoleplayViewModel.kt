package com.iti.linguaquest.features.roleplay.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.roleplay.data.repository.RoleplayRepositoryImpl
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleplayViewModel @Inject constructor(
    private val repository: RoleplayRepositoryImpl,
    private val getTargetLanguageNameUseCase: GetTargetLanguageNameUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(RoleplayState())
    val state: StateFlow<RoleplayState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RoleplayEffect>()
    val effect: SharedFlow<RoleplayEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            getTargetLanguageNameUseCase().collect { lang ->
                _state.update { it.copy(targetLanguage = lang ?: "English") }
            }
        }

        // Observe continuous server events from the live session
        viewModelScope.launch {
            repository.events.collect { event ->
                handleLiveEvent(event)
            }
        }
    }

    fun onIntent(intent: RoleplayIntent) {
        when (intent) {
            RoleplayIntent.StartLevelClicked -> initializeLiveSession()
            RoleplayIntent.RecordClicked -> toggleMicrophone(true)
            RoleplayIntent.StopRecordingClicked -> toggleMicrophone(false)
            RoleplayIntent.ReturnHomeClicked -> {
                viewModelScope.launch { repository.disconnect() }
                sendEffect(RoleplayEffect.NavigateToHome)
            }
            else -> Unit // RetryClicked and AiAudioFinished handled differently in live mode
        }
    }

    private fun initializeLiveSession() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val systemPrompt = "You are Lingo. Setting: ${_state.value.setting}. Objective: ${_state.value.objectiveText}."
            
            try {
                repository.connect(systemPrompt)
                _state.update { it.copy(isConnected = true, isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                handleError("Failed to connect: ${e.message}")
            }
        }
    }

    private fun toggleMicrophone(active: Boolean) {
        _state.update { it.copy(isUserSpeaking = active) }
        if (active) {
            repository.startMicrophone()
        } else {
            repository.stopMicrophone()
        }
    }

    private fun handleLiveEvent(event: RoleplayLiveEvent) {
        when (event) {
            is RoleplayLiveEvent.Transcription -> {
                val newHistory = _state.value.transcriptionHistory + event.text
                _state.update { 
                    it.copy(
                        transcriptionHistory = newHistory,
                        isAiSpeaking = true
                    ) 
                }
            }
            is RoleplayLiveEvent.TurnComplete -> {
                _state.update { it.copy(isAiSpeaking = false) }
            }
            is RoleplayLiveEvent.Error -> {
                handleError(event.message)
            }
            is RoleplayLiveEvent.AudioChunk -> {
                // Instantly handled by Repository -> AudioPlayer
            }
        }
    }

    private fun handleError(message: String) {
        viewModelScope.launch {
            snackbarController.sendEvent(
                SnackbarEvent(
                    message = UiText.DynamicString(message),
                    type = SnackbarType.ERROR
                )
            )
        }
    }

    private fun sendEffect(effect: RoleplayEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { repository.disconnect() }
    }
}
