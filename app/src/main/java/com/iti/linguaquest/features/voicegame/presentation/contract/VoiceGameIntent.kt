package com.iti.linguaquest.features.voicegame.presentation.contract

sealed interface VoiceGameIntent {
    data class Init(val sentence: String, val lessonId: Int) : VoiceGameIntent
    data object ListenClicked : VoiceGameIntent
    data object RecordClicked : VoiceGameIntent
    data object MicPermissionGranted : VoiceGameIntent
    data object MicPermissionDenied : VoiceGameIntent
    data object PauseClicked : VoiceGameIntent
    data object ResumeClicked : VoiceGameIntent
    data object DoneClicked : VoiceGameIntent
    data object CancelRecordingClicked : VoiceGameIntent
    data object DiscardClicked : VoiceGameIntent
    data object TogglePreviewPlaybackClicked : VoiceGameIntent
    data object ConfirmProcessClicked : VoiceGameIntent
    data object SkipClicked : VoiceGameIntent
    data object GenerateNewSentenceClicked : VoiceGameIntent
}