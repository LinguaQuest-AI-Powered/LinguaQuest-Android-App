package com.iti.linguaquest.features.roleplay.presentation.contract

sealed interface RoleplayIntent {
    data object StartLevelClicked : RoleplayIntent
    data object RecordClicked : RoleplayIntent
    data object StopRecordingClicked : RoleplayIntent
    data object AiAudioFinished : RoleplayIntent
    data object RetryClicked : RoleplayIntent
    data object ReturnHomeClicked : RoleplayIntent
    
    // Boss Stage Intents
    data class LoadBossLobby(val scenarioId: com.iti.linguaquest.features.roleplay.domain.model.ScenarioId) : RoleplayIntent
    data object StartBossStageClicked : RoleplayIntent
    data object FinishStageClicked : RoleplayIntent
    data object RetryStageClicked : RoleplayIntent
    data object AdvanceToNextWorldClicked : RoleplayIntent
}
