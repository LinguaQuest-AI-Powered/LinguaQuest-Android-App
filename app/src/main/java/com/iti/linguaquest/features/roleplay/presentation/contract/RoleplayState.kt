package com.iti.linguaquest.features.roleplay.presentation.contract

enum class RoleplayPhase { LOBBY, IDLE, RECORDING, PROCESSING, AI_SPEAKING, OUTCOME }

data class RoleplayState(
    val phase: RoleplayPhase = RoleplayPhase.LOBBY,
    val objectiveText: String = "",
    val setting: String = "",
    val targetLanguage: String = "French",
    val aiResponseText: String = "",
    val aiTranslation: String = "",
    val turnCount: Int = 0,
    val maxTurns: Int = 4,
    val coinsEarned: Int = 0,
    val feedback: String = "",
    val isPassed: Boolean = false,
    val isLoading: Boolean = false,
    val isObjectiveComplete: Boolean = false,
    val recordingElapsedSeconds: Int = 0
)
