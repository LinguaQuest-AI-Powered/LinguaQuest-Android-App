package com.iti.linguaquest.features.roleplay.presentation.contract

import com.iti.linguaquest.core.sharedComponents.text.UiText
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.BossEvaluationResult
import com.iti.linguaquest.features.roleplay.domain.model.ChatMessage

data class RoleplayState(
    val objectiveText: String = "",
    val setting: String = "",
    val targetLanguage: String = "English",
    val isConnected: Boolean = false,
    val isUserSpeaking: Boolean = false,
    val isAiSpeaking: Boolean = false,
    val transcriptionHistory: List<ChatMessage> = emptyList(),
    val isObjectiveComplete: Boolean = false,
    val isEvaluating: Boolean = false,
    val remainingTimeSeconds: Int = 120,
    val isTimerRunning: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val currentBossScenario: BossScenario? = null,
    val assessmentResult: BossEvaluationResult? = null
)
