package com.iti.linguaquest.features.roleplay.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.audio.domain.usecase.RecordAudioUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.EvaluateRoleplayUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.InitializeRoleplayUseCase
import com.iti.linguaquest.features.roleplay.domain.usecase.SubmitUserAudioUseCase
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayPhase
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class RoleplayViewModel @Inject constructor(
    private val initializeRoleplayUseCase: InitializeRoleplayUseCase,
    private val submitUserAudioUseCase: SubmitUserAudioUseCase,
    private val evaluateRoleplayUseCase: EvaluateRoleplayUseCase,
    private val recordAudioUseCase: RecordAudioUseCase,
    private val getTargetLanguageNameUseCase: GetTargetLanguageNameUseCase,
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
    }

    fun onIntent(intent: RoleplayIntent) {
        when (intent) {
            RoleplayIntent.StartLevelClicked -> initializeLevel()
            RoleplayIntent.RecordClicked -> startRecording()
            RoleplayIntent.StopRecordingClicked -> stopRecordingAndSubmit()
            RoleplayIntent.AiAudioFinished -> onAiAudioFinished()
            RoleplayIntent.RetryClicked -> retry()
            RoleplayIntent.ReturnHomeClicked -> sendEffect(RoleplayEffect.NavigateToHome)
        }
    }

    private fun initializeLevel() {
        viewModelScope.launch {
            _state.update { it.copy(phase = RoleplayPhase.PROCESSING, isLoading = true) }
            when (val result = initializeRoleplayUseCase(
                setting = _state.value.setting,
                taskDescription = _state.value.objectiveText
            )) {
                is LinguaQuestResult.Success -> applyTurnResponse(result.data)
                is LinguaQuestResult.Failure -> handleError("Failed to start roleplay session", RoleplayPhase.LOBBY)
            }
        }
    }

    private fun startRecording() {
        recordAudioUseCase.start()
        _state.update { it.copy(phase = RoleplayPhase.RECORDING, recordingElapsedSeconds = 0) }
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000.milliseconds)
                _state.update { it.copy(recordingElapsedSeconds = it.recordingElapsedSeconds + 1) }
            }
        }
    }

    private fun stopRecordingAndSubmit() {
        timerJob?.cancel()
        val pcmData = recordAudioUseCase.stopAndGetPcmData()
        _state.update { it.copy(phase = RoleplayPhase.PROCESSING, isLoading = true) }

        viewModelScope.launch {
            when (val result = submitUserAudioUseCase(pcmData)) {
                is LinguaQuestResult.Success -> applyTurnResponse(result.data)
                is LinguaQuestResult.Failure -> handleError("Failed to process your response", RoleplayPhase.IDLE)
            }
        }
    }

    private fun onAiAudioFinished() {
        if (_state.value.isObjectiveComplete) {
            evaluateRoleplay()
        } else {
            _state.update { it.copy(phase = RoleplayPhase.IDLE) }
        }
    }

    private fun evaluateRoleplay() {
        viewModelScope.launch {
            _state.update { it.copy(phase = RoleplayPhase.PROCESSING, isLoading = true) }
            when (val result = evaluateRoleplayUseCase()) {
                is LinguaQuestResult.Success -> {
                    val outcome = result.data
                    _state.update {
                        it.copy(
                            phase = RoleplayPhase.OUTCOME,
                            isPassed = outcome.passed,
                            coinsEarned = outcome.coinsAwarded,
                            feedback = outcome.feedback,
                            isLoading = false
                        )
                    }
                }
                is LinguaQuestResult.Failure -> handleError("Failed to evaluate roleplay", RoleplayPhase.IDLE)
            }
        }
    }

    private fun retry() {
        _state.update {
            RoleplayState(
                setting = it.setting,
                objectiveText = it.objectiveText,
                maxTurns = it.maxTurns
            )
        }
    }

    private fun applyTurnResponse(response: com.iti.linguaquest.features.roleplay.domain.model.RoleplayTurnResponse) {
        _state.update {
            it.copy(
                phase = RoleplayPhase.AI_SPEAKING,
                aiResponseText = response.aiText,
                aiTranslation = response.aiTranslation,
                turnCount = response.turnNumber,
                isLoading = false,
                isObjectiveComplete = response.isObjectiveComplete
            )
        }
        sendEffect(RoleplayEffect.PlayAiAudio(response.audioBytes))
    }

    private fun handleError(message: String, fallbackPhase: RoleplayPhase) {
        _state.update { it.copy(phase = fallbackPhase, isLoading = false) }
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
        recordAudioUseCase.discard()
    }
}
