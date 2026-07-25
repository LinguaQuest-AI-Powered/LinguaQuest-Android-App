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
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
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

    private var timerJob: Job? = null

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
                _state.update { it.copy(isConnected = true, isLoading = false, isUserSpeaking = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                handleError("Failed to connect: ${e.message}")
            }
        }
    }

    fun endRoleplay() {
        viewModelScope.launch {
            timerJob?.cancel()
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
                _state.update { it.copy(isConnected = true, isLoading = false, isUserSpeaking = false) }
                
                timerJob?.cancel()
                timerJob = viewModelScope.launch {
                    _state.update { it.copy(isTimerRunning = true, remainingTimeSeconds = 120) }
                    while (_state.value.remainingTimeSeconds > 0) {
                        delay(1000)
                        _state.update { it.copy(remainingTimeSeconds = it.remainingTimeSeconds - 1) }
                    }
                    if (_state.value.isConnected) {
                        finishBossStage()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false, error = e.message) }
                handleError("Failed to connect: ${e.message}")
            }
        }
    }

    private fun finishBossStage() {
        val scenario = _state.value.currentBossScenario ?: return
        
        val userHasSpoken = _state.value.transcriptionHistory.any { it.isUser }
        
        if (!userHasSpoken) {
            viewModelScope.launch {
                timerJob?.cancel()
                stopMicrophoneUseCase()
                disconnectRoleplayUseCase()
                _state.update { 
                    it.copy(
                        isConnected = false, 
                        isUserSpeaking = false, 
                        isEvaluating = false,
                        assessmentResult = com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult(
                            isTaskCompleted = false,
                            fluencyScore = 0,
                            feedbackMessage = "You didn't say anything! Please try again and speak to the character."
                        )
                    )
                }
            }
            return
        }
        
        val transcript = _state.value.transcriptionHistory.map { msg ->
            if (msg.isUser) "User: ${msg.text}" else "AI: ${msg.text}"
        }
        
        viewModelScope.launch {
            timerJob?.cancel()
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
                isUserSpeaking = false,
                error = null
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
                val history = _state.value.transcriptionHistory.toMutableList()
                if (history.isNotEmpty() && history.last().isUser == event.isUser) {
                    val lastMsg = history.removeLast()
                    history.add(lastMsg.copy(text = lastMsg.text + event.text))
                } else {
                    history.add(com.iti.linguaquest.features.roleplay.presentation.model.ChatMessage(event.text, event.isUser))
                }
                
                _state.update { 
                    it.copy(
                        transcriptionHistory = history,
                        isAiSpeaking = if (!event.isUser) true else it.isAiSpeaking
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
        _state.update { it.copy(error = message) }
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
        timerJob?.cancel()
        viewModelScope.launch { disconnectRoleplayUseCase() }
    }
}
