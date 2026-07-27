package com.iti.linguaquest.features.voicegame.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.audio.domain.usecase.PlayAudioPreviewUseCase
import com.iti.linguaquest.core.audio.domain.usecase.RecordAudioUseCase
import com.iti.linguaquest.core.audio.domain.usecase.SpeakTextUseCase
import com.iti.linguaquest.core.connectivity.domain.ObserveNetworkStatusUseCase
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import com.iti.linguaquest.core.wallet.domain.usecase.AdjustWalletUseCase
import com.iti.linguaquest.core.wallet.domain.usecase.GetWalletUseCase
import com.iti.linguaquest.features.onBoarding.domain.usecase.GetTargetLanguageNameUseCase
import com.iti.linguaquest.features.voicegame.domain.usecase.EvaluatePronunciationUseCase
import com.iti.linguaquest.features.voicegame.domain.usecase.GeneratePronunciationSentenceUseCase
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameEffect
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameIntent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGamePhase
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameState
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class VoiceGameViewModel @Inject constructor(
    private val speakTextUseCase: SpeakTextUseCase,
    private val recordAudioUseCase: RecordAudioUseCase,
    private val playAudioPreviewUseCase: PlayAudioPreviewUseCase,
    private val evaluatePronunciationUseCase: EvaluatePronunciationUseCase,
    private val generatePronunciationSentenceUseCase: GeneratePronunciationSentenceUseCase,
    private val getTargetLanguageNameUseCase: GetTargetLanguageNameUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val adjustWalletUseCase: AdjustWalletUseCase,
    private val snackbarController: SnackbarController
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = observeNetworkStatusUseCase()

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )


    private val _state = MutableStateFlow(VoiceGameState())
    val state: StateFlow<VoiceGameState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<VoiceGameEffect>()
    val effect: SharedFlow<VoiceGameEffect> = _effect.asSharedFlow()
    val wallet = getWalletUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Wallet(xp = 0, coins = 0)
    )
    private var timerJob: Job? = null
    private var previewTickJob: Job? = null
    private var lessonId: Int = 0
    private var pendingPcmData: ByteArray? = null
    private var previewFile: File? = null

    private val topics = listOf(
        "General Conversation",
        "Daily Life",
        "Greetings",
        "Food & Dining",
        "Weather",
        "Travel",
        "Hobbies",
        "Family & Friends"
    )

    init {
        viewModelScope.launch {
            getTargetLanguageNameUseCase().collect { lang ->
                _state.update { it.copy(targetLanguage = lang ?: "English") }
            }
        }
    }

    fun onIntent(intent: VoiceGameIntent) {
        when (intent) {
            is VoiceGameIntent.Init -> {
                generateNewSentence()
            }

            VoiceGameIntent.SkipClicked,
            VoiceGameIntent.GenerateNewSentenceClicked -> generateNewSentence()

            VoiceGameIntent.ListenClicked -> speakTextUseCase(_state.value.sentence)
            VoiceGameIntent.RecordClicked -> sendEffect(VoiceGameEffect.RequestMicPermission)
            VoiceGameIntent.MicPermissionGranted -> startRecording()
            VoiceGameIntent.MicPermissionDenied -> viewModelScope.launch {
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = UiText.DynamicString("Microphone access is required to record your voice"),
                        type = SnackbarType.ERROR
                    )
                )
            }

            VoiceGameIntent.PauseClicked -> {
                recordAudioUseCase.pause()
                _state.update { it.copy(isPaused = true) }
            }

            VoiceGameIntent.ResumeClicked -> {
                recordAudioUseCase.resume()
                _state.update { it.copy(isPaused = false) }
            }

            VoiceGameIntent.DoneClicked -> stopRecordingForReview()
            VoiceGameIntent.CancelRecordingClicked -> resetToIdle(discardAudio = true)
            VoiceGameIntent.DiscardClicked -> resetToIdle(discardAudio = true)
            VoiceGameIntent.TogglePreviewPlaybackClicked -> togglePreviewPlayback()
            VoiceGameIntent.ConfirmProcessClicked -> {
                playAudioPreviewUseCase.stop()
                _state.update {
                    it.copy(
                        showConfirmationDialog = false,
                        phase = VoiceGamePhase.EVALUATING,
                        isPreviewPlaying = false
                    )
                }
                evaluateRecording()
            }
        }
    }

    private fun generateNewSentence() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingSentence = true) }
            try {
                val targetLang = getTargetLanguageNameUseCase().firstOrNull()
                    ?.takeIf { it.isNotBlank() }
                    ?: _state.value.targetLanguage.takeIf { it.isNotBlank() }
                    ?: "English"

                _state.update { it.copy(targetLanguage = targetLang) }

                val currentTopic = topics.random()
                when (val result = generatePronunciationSentenceUseCase(
                    targetLanguage = targetLang,
                    topic = currentTopic
                )) {
                    is LinguaQuestResult.Success -> {
                        _state.update {
                            it.copy(
                                sentence = result.data.sentence,
                                phonetic = result.data.phonetic,
                                translation = result.data.translation,
                                isLoadingSentence = false
                            )
                        }
                    }

                    is LinguaQuestResult.Failure -> {
                        _state.update { it.copy(isLoadingSentence = false) }
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = UiText.DynamicString("Failed to generate sentence"),
                                type = SnackbarType.ERROR
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoadingSentence = false) }
            }
        }
    }

    private fun startRecording() {
        recordAudioUseCase.start()
        _state.update {
            it.copy(
                phase = VoiceGamePhase.RECORDING,
                recordingElapsedSeconds = 0,
                isPaused = false
            )
        }
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000.milliseconds)
                if (!_state.value.isPaused) {
                    _state.update { it.copy(recordingElapsedSeconds = it.recordingElapsedSeconds + 1) }
                }
            }
        }
    }

    private fun stopRecordingForReview() {
        timerJob?.cancel()
        val pcmData = recordAudioUseCase.stopAndGetPcmData()
        pendingPcmData = pcmData
        previewFile = recordAudioUseCase.savePcmAsWav(pcmData)
        _state.update {
            it.copy(
                showConfirmationDialog = true,
                previewDurationSeconds = it.recordingElapsedSeconds,
                previewPlaybackSeconds = it.recordingElapsedSeconds
            )
        }
    }

    private fun togglePreviewPlayback() {
        val file = previewFile ?: return
        if (_state.value.isPreviewPlaying) {
            stopPreviewPlayback()
        } else {
            _state.update { it.copy(isPreviewPlaying = true, previewPlaybackSeconds = 0) }
            playAudioPreviewUseCase.play(file) {
                previewTickJob?.cancel()
                _state.update {
                    it.copy(
                        isPreviewPlaying = false,
                        previewPlaybackSeconds = it.previewDurationSeconds
                    )
                }
            }
            previewTickJob = viewModelScope.launch {
                while (_state.value.previewPlaybackSeconds < _state.value.previewDurationSeconds) {
                    delay(1000.milliseconds)
                    _state.update {
                        it.copy(
                            previewPlaybackSeconds = (it.previewPlaybackSeconds + 1).coerceAtMost(
                                it.previewDurationSeconds
                            )
                        )
                    }
                }
            }
        }
    }

    private fun stopPreviewPlayback() {
        previewTickJob?.cancel()
        playAudioPreviewUseCase.stop()
        _state.update {
            it.copy(isPreviewPlaying = false, previewPlaybackSeconds = it.previewDurationSeconds)
        }
    }

    private fun resetToIdle(discardAudio: Boolean) {
        timerJob?.cancel()
        playAudioPreviewUseCase.stop()
        if (discardAudio) recordAudioUseCase.discard()
        pendingPcmData = null
        previewFile = null
        _state.update {
            it.copy(
                phase = VoiceGamePhase.IDLE,
                showConfirmationDialog = false,
                recordingElapsedSeconds = 0,
                isPaused = false,
                isPreviewPlaying = false,
                previewDurationSeconds = 0
            )
        }
    }

    private fun evaluateRecording() {
        val pcmData = pendingPcmData ?: ByteArray(0)
        if (pcmData.isEmpty()) {
            resetToIdle(discardAudio = true)
            return
        }

        viewModelScope.launch {
            try {
                when (val result = evaluatePronunciationUseCase(
                    targetSentence = _state.value.sentence,
                    targetLanguage = _state.value.targetLanguage,
                    audioBytes = pcmData
                )) {
                    is LinguaQuestResult.Success -> {
                        val evaluation = result.data
                        val passed = evaluation.rating >= 6 // Pass criteria: 6/10 or higher

                        val voiceResult = VoiceResultUi(
                            rating = evaluation.rating,
                            correctWords = evaluation.correctWords,
                            wrongWords = evaluation.wrongWords,
                            advice = evaluation.advice,
                            coinsAwarded = if (passed) 10 else 0,
                            isPassed = passed,
                            lessonId = lessonId,
                            sentence = _state.value.sentence,
                            coinsBeforeAward = wallet.value.coins
                        )

                        sendEffect(VoiceGameEffect.NavigateToResult(voiceResult))
                        onGameWon(coinsDelta = voiceResult.coinsAwarded)
                        resetToIdle(discardAudio = false)
                    }

                    is LinguaQuestResult.Failure -> {
                        snackbarController.sendEvent(
                            SnackbarEvent(
                                message = UiText.DynamicString("Failed to evaluate pronunciation"),
                                type = SnackbarType.ERROR
                            )
                        )
                        resetToIdle(discardAudio = true)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                snackbarController.sendEvent(
                    SnackbarEvent(
                        message = UiText.DynamicString("Error: ${e.localizedMessage ?: "Failed to evaluate"}"),
                        type = SnackbarType.ERROR
                    )
                )
                resetToIdle(discardAudio = true)
            }
        }
    }

    private fun sendEffect(effect: VoiceGameEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    fun onGameWon(xpDelta: Int = 0, coinsDelta: Int = 5) {
        viewModelScope.launch {
            adjustWalletUseCase(xpDelta = xpDelta, coinsDelta = coinsDelta)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        playAudioPreviewUseCase.stop()
        recordAudioUseCase.discard()
    }
}