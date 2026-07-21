package com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract

enum class ProficiencyLevel(val displayName: String, val description: String) {
    BEGINNER("Beginner", "I'm just starting my adventure"),
    INTERMEDIATE("Intermediate", "I can navigate basic paths"),
    ADVANCED("Advanced", "Ready for grand challenges")
}

data class LevelState(
    val selectedLevel: ProficiencyLevel = ProficiencyLevel.BEGINNER,
    val isContinueEnabled: Boolean = true
)