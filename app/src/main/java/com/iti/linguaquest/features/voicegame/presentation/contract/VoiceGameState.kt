package com.iti.linguaquest.features.voicegame.presentation.contract

enum class VoiceGamePhase { IDLE, RECORDING, EVALUATING }

data class VoiceGameState(
    val sentence: String = "",
    val targetLanguage: String = "English",
    val phase: VoiceGamePhase = VoiceGamePhase.IDLE,
    val recordingElapsedSeconds: Int = 0,
    val isPaused: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val previewDurationSeconds: Int = 0,
    val previewPlaybackSeconds: Int = 0,
    val isPreviewPlaying: Boolean = false,
    val isLoadingSentence: Boolean = false,
    val phonetic: String? = null,
    val translation: String? = null,
    val dailyWord: String? = null
)