package com.iti.linguaquest.features.roleplay.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.model.BossScenarioProvider
import com.iti.linguaquest.features.roleplay.domain.usecase.ConnectToBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.ConnectToFreePlayUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.DisconnectRoleplayUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.EvaluateBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.ObserveLiveEventsUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StartMicrophoneUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StopMicrophoneUseCase
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
    private val getTargetLanguageNameUseCase: GetTargetLanguageNameUseCase,
    private val connectToFreePlayUseCase: ConnectToFreePlayUseCase,
    private val connectToBossStageUseCase: ConnectToBossStageUseCase,
    private val evaluateBossStageUseCase: EvaluateBossStageUseCase,
    private val startMicrophoneUseCase: StartMicrophoneUseCase,
    private val stopMicrophoneUseCase: StopMicrophoneUseCase,
    private val disconnectRoleplayUseCase: DisconnectRoleplayUseCase,
    private val observeLiveEventsUseCase: ObserveLiveEventsUseCase,
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
            observeLiveEventsUseCase().collect { event ->
                handleLiveEvent(event)
            }
        }
    }

    fun onIntent(intent: RoleplayIntent) {
        when (intent) {
            RoleplayIntent.StartLevelClicked -> startRoleplay()
            RoleplayIntent.RecordClicked -> toggleMicrophone(true)
            RoleplayIntent.StopRecordingClicked -> toggleMicrophone(false)
            RoleplayIntent.ReturnHomeClicked -> {
                endRoleplay()
                sendEffect(RoleplayEffect.NavigateToHome)
            }
            is RoleplayIntent.LoadBossLobby -> loadBossLobby(intent.scenarioId)
            RoleplayIntent.StartBossStageClicked -> startBossStage()
            RoleplayIntent.FinishStageClicked -> finishBossStage()
            RoleplayIntent.RetryStageClicked -> retryBossStage()
            RoleplayIntent.AdvanceToNextWorldClicked -> sendEffect(RoleplayEffect.NavigateToHome)
            else -> Unit // RetryClicked and AiAudioFinished handled differently in live mode
        }
    }

    fun startRoleplay() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                connectToFreePlayUseCase(state.value.targetLanguage)
                startMicrophoneUseCase()
                _state.update { it.copy(isConnected = true, isLoading = false, isUserSpeaking = true) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                handleError("Failed to connect: ${e.message}")
            }
        }
    }

    fun endRoleplay() {
        viewModelScope.launch {
            stopMicrophoneUseCase()
            disconnectRoleplayUseCase()
            _state.update { it.copy(isConnected = false, isUserSpeaking = false) }
        }
    }

    private fun loadBossLobby(scenarioId: String) {
        val scenario = BossScenarioProvider.scenarios.find { it.id == scenarioId }
        _state.update { it.copy(currentBossScenario = scenario) }
    }

    private fun startBossStage() {
        val scenario = _state.value.currentBossScenario ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                connectToBossStageUseCase(scenario)
                startMicrophoneUseCase()
                _state.update { it.copy(isConnected = true, isLoading = false, isUserSpeaking = true) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                handleError("Failed to connect: ${e.message}")
            }
        }
    }

    private fun finishBossStage() {
        val scenario = _state.value.currentBossScenario ?: return
        val transcript = _state.value.transcriptionHistory
        
        viewModelScope.launch {
            stopMicrophoneUseCase()
            disconnectRoleplayUseCase()
            _state.update { 
                it.copy(isConnected = false, isUserSpeaking = false, isEvaluating = true)
            }
            
            val result = evaluateBossStageUseCase(transcript, scenario)
            result.onSuccess { assessmentResult ->
                _state.update { 
                    it.copy(isEvaluating = false, assessmentResult = assessmentResult) 
                }
            }.onFailure { e ->
                _state.update { it.copy(isEvaluating = false) }
                handleError("Connection Lost: ${e.message}")
                sendEffect(RoleplayEffect.NavigateToHome)
            }
        }
    }

    private fun retryBossStage() {
        _state.update { 
            it.copy(
                transcriptionHistory = emptyList(),
                assessmentResult = null,
                isEvaluating = false,
                isAiSpeaking = false,
                isUserSpeaking = false
            ) 
        }
        startBossStage()
    }

    private fun toggleMicrophone(active: Boolean) {
        _state.update { it.copy(isUserSpeaking = active) }
        if (active) {
            startMicrophoneUseCase()
        } else {
            stopMicrophoneUseCase()
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
        viewModelScope.launch { disconnectRoleplayUseCase() }
    }
}
