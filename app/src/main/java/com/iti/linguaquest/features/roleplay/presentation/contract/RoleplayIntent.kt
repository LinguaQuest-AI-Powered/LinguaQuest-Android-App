package com.iti.linguaquest.features.roleplay.presentation.contract

sealed interface RoleplayIntent {
    data object StartLevelClicked : RoleplayIntent
    data object RecordClicked : RoleplayIntent
    data object StopRecordingClicked : RoleplayIntent
    data object AiAudioFinished : RoleplayIntent
    data object RetryClicked : RoleplayIntent
    data object ReturnHomeClicked : RoleplayIntent
}
