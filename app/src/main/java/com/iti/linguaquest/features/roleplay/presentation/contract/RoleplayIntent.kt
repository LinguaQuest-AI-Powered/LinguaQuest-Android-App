package com.iti.linguaquest.features.roleplay.presentation.contract

import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId

sealed interface RoleplayIntent {
    data object RecordClicked : RoleplayIntent
    data object StopRecordingClicked : RoleplayIntent
    data object AiAudioFinished : RoleplayIntent
    data object RetryClicked : RoleplayIntent
    data object ReturnHomeClicked : RoleplayIntent
    

    data class LoadBossLobby(val scenarioId: ScenarioId) : RoleplayIntent
    data object StartBossStageClicked : RoleplayIntent
    data object FinishStageClicked : RoleplayIntent
    data object RetryStageClicked : RoleplayIntent
    data object AdvanceToNextWorldClicked : RoleplayIntent
}
