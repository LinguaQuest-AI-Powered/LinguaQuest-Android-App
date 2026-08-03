package com.iti.linguaquest.features.roleplay.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.repository.ScenarioRepository
import com.iti.linguaquest.features.roleplay.domain.usecase.ConnectToBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.DisconnectRoleplayUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.EvaluateBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.ObserveLiveEventsUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StartMicrophoneUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StopMicrophoneUseCase
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.presentation.model.ChatMessage
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.AppSoundPlayer
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class RoleplayViewModel @Inject constructor(
    private val getTargetLanguageNameUseCase: GetTargetLanguageNameUseCase,
    private val connectToBossStageUseCase: ConnectToBossStageUseCase,
    private val evaluateBossStageUseCase: EvaluateBossStageUseCase,
    private val startMicrophoneUseCase: StartMicrophoneUseCase,
    private val stopMicrophoneUseCase: StopMicrophoneUseCase,
    private val disconnectRoleplayUseCase: DisconnectRoleplayUseCase,
    private val observeLiveEventsUseCase: ObserveLiveEventsUseCase,
    private val scenarioRepository: ScenarioRepository,
    private val snackbarController: SnackbarController,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val soundPlayer: AppSoundPlayer
) : ViewModel() {

    private val _state = MutableStateFlow(RoleplayState())
    val state: StateFlow<RoleplayState> = _state.asStateFlow()

    val wallet: StateFlow<Wallet> = getWalletUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Wallet(xp = 0, coins = 0)
    )

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )

    private val _effect = MutableSharedFlow<RoleplayEffect>()
    val effect: SharedFlow<RoleplayEffect> = _effect.asSharedFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            getTargetLanguageNameUseCase().collect { lang ->
                _state.update { it.copy(targetLanguage = lang ?: "English") }
            }
        }


        viewModelScope.launch {
            observeLiveEventsUseCase().collect { event ->
                handleLiveEvent(event)
            }
        }
    }

    fun onIntent(intent: RoleplayIntent) {
        when (intent) {

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
            else -> Unit
        }
    }

    fun endRoleplay() {
        viewModelScope.launch {
            timerJob?.cancel()
            if (_state.value.isUserSpeaking) {
                soundPlayer.play(AppSound.CLOSE_MIC)
            }
            stopMicrophoneUseCase()
            disconnectRoleplayUseCase()
            _state.update { it.copy(isConnected = false, isUserSpeaking = false) }
        }
    }

    private fun loadBossLobby(scenarioId: ScenarioId) {
        viewModelScope.launch {
            try {
                val lang = java.util.Locale.getDefault().language
                val scenarios = scenarioRepository.getBossScenarios(lang)
                val scenario = scenarios.find { it.id == scenarioId }
                _state.update { it.copy(currentBossScenario = scenario) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
                _state.update { it.copy(isLoading = false, error = UiText.StringResource(R.string.roleplay_failed_connect, listOf(e.message ?: ""))) }
                handleError(UiText.StringResource(R.string.roleplay_failed_connect, listOf(e.message ?: "")))
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
                        assessmentResult = BossEvaluationResult(
                            task_completed = false,
                            fluency_score = 0,
                            feedback_message = "ERROR_NO_SPEECH"
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
                handleError(UiText.StringResource(R.string.roleplay_connection_lost, listOf(e.message ?: "")))
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
            soundPlayer.play(AppSound.OPEN_MIC)
            startMicrophoneUseCase()
        } else {
            soundPlayer.play(AppSound.CLOSE_MIC)
            stopMicrophoneUseCase()
        }
    }

    private fun handleLiveEvent(event: RoleplayLiveEvent) {
        when (event) {
            is RoleplayLiveEvent.Transcription -> {
                val history = _state.value.transcriptionHistory.toMutableList()
                if (history.isNotEmpty() && history.last().isUser == event.isUser) {
                    val lastMsg = history.removeAt(history.size - 1)
                    history.add(lastMsg.copy(text = lastMsg.text + event.text))
                } else {
                    history.add(ChatMessage(event.text, event.isUser))
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
                handleError(UiText.DynamicString(event.message))
            }
            is RoleplayLiveEvent.AudioChunk -> {

            }
        }
    }

    private fun handleError(message: UiText) {
        _state.update { it.copy(error = message) }
        viewModelScope.launch {
            snackbarController.sendEvent(
                SnackbarEvent(
                    message = message,
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
