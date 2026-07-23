package com.iti.linguaquest.features.voicegame.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.linguaquest.core.audio.AudioPlayerController
import com.iti.linguaquest.core.audio.AudioRecorderController
import com.iti.linguaquest.core.audio.TextToSpeechController
import com.iti.linguaquest.core.audio.writePcmAsWavFile
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarController
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarEvent
import com.iti.linguaquest.core.sharedComponents.snackbar.SnackbarType
import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameEffect
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameIntent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGamePhase
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameState
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.features.voicegame.data.remote.VoiceEvaluationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.voicegame.domain.usecase.GeneratePronunciationSentenceUseCase

@HiltViewModel
class VoiceGameViewModel @Inject constructor(
    private val audioRecorder: AudioRecorderController,
    private val audioPlayer: AudioPlayerController,
    private val textToSpeech: TextToSpeechController,
    private val snackbarController: SnackbarController,
    private val voiceEvaluationService: VoiceEvaluationService,
    private val generatePronunciationSentenceUseCase: GeneratePronunciationSentenceUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(VoiceGameState())
    val state: StateFlow<VoiceGameState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<VoiceGameEffect>()
    val effect: SharedFlow<VoiceGameEffect> = _effect.asSharedFlow()

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
            userPreferencesRepository.targetLanguageName.collect { lang ->
                _state.update { it.copy(targetLanguage = lang ?: "English") }
            }
        }
    }

    fun onIntent(intent: VoiceGameIntent) {
        when (intent) {
            is VoiceGameIntent.Init -> {
                lessonId = intent.lessonId
                generateNewSentence()
            }

            VoiceGameIntent.SkipClicked,
            VoiceGameIntent.GenerateNewSentenceClicked -> generateNewSentence()

            VoiceGameIntent.ListenClicked -> textToSpeech.speak(_state.value.sentence)
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
                audioRecorder.pause()
                _state.update { it.copy(isPaused = true) }
            }

            VoiceGameIntent.ResumeClicked -> {
                audioRecorder.resume()
                _state.update { it.copy(isPaused = false) }
            }

            VoiceGameIntent.DoneClicked -> stopRecordingForReview()
            VoiceGameIntent.CancelRecordingClicked -> resetToIdle(discardAudio = true)
            VoiceGameIntent.DiscardClicked -> resetToIdle(discardAudio = true)
            VoiceGameIntent.TogglePreviewPlaybackClicked -> togglePreviewPlayback()
            VoiceGameIntent.ConfirmProcessClicked -> {
                audioPlayer.stop()
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
                val currentTopic = topics.random()
                when (val result = generatePronunciationSentenceUseCase(
                    targetLanguage = _state.value.targetLanguage,
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
        audioRecorder.start()
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
        val pcmData = audioRecorder.stopAndGetPcmData()
        pendingPcmData = pcmData
        previewFile = writePcmAsWavFile(context, pcmData, AudioRecorderController.SAMPLE_RATE)
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
            audioPlayer.play(file) {
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
        audioPlayer.stop()
        _state.update {
            it.copy(isPreviewPlaying = false, previewPlaybackSeconds = it.previewDurationSeconds)
        }
    }

    private fun resetToIdle(discardAudio: Boolean) {
        timerJob?.cancel()
        audioPlayer.stop()
        if (discardAudio) audioRecorder.discard()
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
                val evaluation = voiceEvaluationService.evaluatePronunciation(_state.value.sentence, _state.value.targetLanguage, pcmData)
                val passed = evaluation.rating >= 6 // Pass criteria: 6/10 or higher

                val result = VoiceResultUi(
                    rating = evaluation.rating,
                    correctWords = evaluation.correctWords,
                    wrongWords = evaluation.wrongWords,
                    advice = evaluation.advice,
                    coinsAwarded = if (passed) 10 else 0,
                    isPassed = passed,
                    lessonId = lessonId,
                    sentence = _state.value.sentence
                )
                
                sendEffect(VoiceGameEffect.NavigateToResult(result))
                resetToIdle(discardAudio = false)
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

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        audioPlayer.stop()
        audioRecorder.discard()
    }
}