package com.iti.linguaquest.features.roleplay.presentation.contract

import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult

data class RoleplayState(
    val objectiveText: String = "",
    val setting: String = "",
    val targetLanguage: String = "English",
    val isConnected: Boolean = false,
    val isUserSpeaking: Boolean = false,
    val isAiSpeaking: Boolean = false,
    val transcriptionHistory: List<String> = emptyList(),
    val isObjectiveComplete: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentBossScenario: BossScenario? = null,
    val isEvaluating: Boolean = false,
    val assessmentResult: RoleplayAssessmentResult? = null
)
