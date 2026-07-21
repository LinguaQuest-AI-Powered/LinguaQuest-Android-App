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
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class VoiceGameViewModel @Inject constructor(
    private val audioRecorder: AudioRecorderController,
    private val audioPlayer: AudioPlayerController,
    private val textToSpeech: TextToSpeechController,
    private val snackbarController: SnackbarController,
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

    fun onIntent(intent: VoiceGameIntent) {
        when (intent) {
            is VoiceGameIntent.Init -> {
                lessonId = intent.lessonId
                _state.update { it.copy(sentence = intent.sentence) }
            }

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
        viewModelScope.launch {
            delay(2500.milliseconds)

            val result = generateFakeResult(pcmData)
            sendEffect(VoiceGameEffect.NavigateToResult(result))
            resetToIdle(discardAudio = false)
        }
    }

    private fun generateFakeResult(pcmData: ByteArray): VoiceResultUi {
        val words = _state.value.sentence.split(" ").filter { it.isNotBlank() }
        // val passed = pcmData.size % 2 == 0
        val passed = true
        val wrongCount = when {
            words.size <= 1 -> if (passed) 0 else 1
            passed -> 1
            else -> (words.size / 2).coerceAtLeast(1)
        }
        val wrongWords = words.takeLast(wrongCount)
        val correctWords = words.dropLast(wrongCount)
        val focusWord = wrongWords.lastOrNull() ?: words.last()

        val advice = if (passed) {
            "Great job! Try to emphasize the pronunciation of \"$focusWord\" a bit more."
        } else {
            "Almost there, explorer! Let's try \"$focusWord\" one more time together."
        }

        return VoiceResultUi(
            rating = if (passed) 8 else 4,
            correctWords = correctWords,
            wrongWords = wrongWords,
            advice = advice,
            coinsAwarded = if (passed) 10 else 0,
            isPassed = passed,
            lessonId = lessonId,
            sentence = _state.value.sentence
        )
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