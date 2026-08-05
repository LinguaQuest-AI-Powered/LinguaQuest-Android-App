package com.iti.linguaquest.features.roleplay.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.R
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.sharedComponents.text.toUiText
import com.iti.linguaquest.core.utils.TranscriptSanitizer
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.AdjustWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.roleplay.domain.model.ChatMessage
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayLiveEvent
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.domain.usecase.ConnectToBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.DisconnectRoleplayUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.EvaluateBossStageUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.GetBossScenariosUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.ObserveLiveEventsUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StartMicrophoneUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.StopMicrophoneUseCase
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
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
    private val getBossScenariosUseCase: GetBossScenariosUseCase,
    private val snackbarController: SnackbarController,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val adjustWalletUseCase: AdjustWalletUseCase
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
    private var thinkingJob: Job? = null

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
            stopSession()
            _state.update { it.copy(isConnected = false, isUserSpeaking = false) }
        }
    }

    private fun loadBossLobby(scenarioId: ScenarioId) {
        viewModelScope.launch {
            try {
                val lang = Locale.getDefault().language
                val scenarios = getBossScenariosUseCase(lang)
                val scenario = scenarios.find { it.id == scenarioId }
                _state.update { it.copy(currentBossScenario = scenario) }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load boss lobby")
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
                Timber.e(e, "Failed to start boss stage")
                _state.update { it.copy(isLoading = false, error = UiText.StringResource(R.string.roleplay_failed_connect, listOf(e.message ?: ""))) }
                handleError(UiText.StringResource(R.string.roleplay_failed_connect, listOf(e.message ?: "")))
            }
        }
    }

    private fun finishBossStage() {
        val scenario = _state.value.currentBossScenario ?: return
        val history = _state.value.transcriptionHistory
        viewModelScope.launch {
            stopSession()
            _state.update { it.copy(isConnected = false, isUserSpeaking = false, isEvaluating = true) }
            
            evaluateBossStageUseCase(history, scenario)
                .onSuccess { enrichedResult ->
                    _state.update { it.copy(isEvaluating = false, assessmentResult = enrichedResult) }
                    if (enrichedResult.task_completed) {
                        awardRewards(xp = enrichedResult.xp_earned, coins = enrichedResult.coins_earned)
                    }
                }
                .onFailure { e ->
                    Timber.e(e, "Failed to evaluate boss stage")
                    _state.update { it.copy(isEvaluating = false) }
                    handleError(UiText.StringResource(R.string.roleplay_connection_lost, listOf(e.message ?: "")))
                    sendEffect(RoleplayEffect.NavigateToHome)
                }
        }
    }

    private fun awardRewards(xp: Int, coins: Int) {
        if (xp > 0 || coins > 0) {
            viewModelScope.launch {
                when (val result = adjustWalletUseCase(xpDelta = xp, coinsDelta = coins)) {
                    is LinguaQuestResult.Success -> Unit
                    is LinguaQuestResult.Failure -> {
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = result.error.toUiText(),
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                }
            }
        }
    }

    private fun retryBossStage() {
        thinkingJob?.cancel()
        thinkingJob = null
        _state.update { 
            it.copy(
                transcriptionHistory = emptyList(),
                assessmentResult = null,
                isEvaluating = false,
                isAiSpeaking = false,
                isAiThinking = false,
                isUserSpeaking = false,
                error = null
            ) 
        }
        startBossStage()
    }

    private fun toggleMicrophone(active: Boolean) {
        if (active) {
            if (_state.value.isAiSpeaking) return
            thinkingJob?.cancel()
            thinkingJob = null
            _state.update { it.copy(isUserSpeaking = true, isAiThinking = false) }
            startMicrophoneUseCase()
        } else {
            if (_state.value.isUserSpeaking) {
                _state.update { it.copy(isUserSpeaking = false, isAiThinking = true) }
                stopMicrophoneUseCase()

                thinkingJob?.cancel()
                thinkingJob = viewModelScope.launch {
                    delay(5000)
                    _state.update { it.copy(isAiThinking = false) }
                }
            }
        }
    }

    private fun handleLiveEvent(event: RoleplayLiveEvent) {
        when (event) {
            is RoleplayLiveEvent.Transcription -> {
                val sanitizedChunk = TranscriptSanitizer.sanitize(event.text, _state.value.targetLanguage)
                val history = _state.value.transcriptionHistory.toMutableList()
                if (history.isNotEmpty() && history.last().isUser == event.isUser) {
                    val lastMsg = history.removeAt(history.size - 1)
                    history.add(lastMsg.copy(text = lastMsg.text + sanitizedChunk))
                } else {
                    history.add(ChatMessage(sanitizedChunk, event.isUser))
                }
                
                if (!event.isUser) {
                    thinkingJob?.cancel()
                    thinkingJob = null
                }
                
                _state.update { 
                    it.copy(
                        transcriptionHistory = history,
                        isAiSpeaking = if (!event.isUser) true else it.isAiSpeaking,
                        isAiThinking = if (!event.isUser) false else it.isAiThinking
                    ) 
                }
            }
            is RoleplayLiveEvent.TurnComplete -> {
                thinkingJob?.cancel()
                thinkingJob = null
                _state.update { it.copy(isAiSpeaking = false, isAiThinking = false) }
            }
            is RoleplayLiveEvent.Error -> {
                thinkingJob?.cancel()
                thinkingJob = null
                val wasActive = _state.value.isConnected || _state.value.isLoading
                viewModelScope.launch {
                    stopSession()
                }
                _state.update {
                    it.copy(
                        isConnected = false,
                        isLoading = false,
                        isUserSpeaking = false,
                        isAiSpeaking = false,
                        isAiThinking = false
                    )
                }
                if (wasActive) {
                    val errorText = UiText.StringResource(R.string.roleplay_connection_lost, listOf(event.message))
                    handleError(errorText)
                    sendEffect(RoleplayEffect.ShowSnackbarAndNavigateBack(errorText))
                } else {
                    handleError(UiText.DynamicString(event.message))
                }
            }
            is RoleplayLiveEvent.AudioChunk -> {
                thinkingJob?.cancel()
                thinkingJob = null
                if (!_state.value.isAiSpeaking) {
                    _state.update { it.copy(isAiSpeaking = true, isAiThinking = false) }
                }
            }
        }
    }

    private suspend fun stopSession() {
        timerJob?.cancel()
        timerJob = null
        thinkingJob?.cancel()
        thinkingJob = null
        stopMicrophoneUseCase()
        disconnectRoleplayUseCase()
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
        viewModelScope.launch { stopSession() }
    }
}
