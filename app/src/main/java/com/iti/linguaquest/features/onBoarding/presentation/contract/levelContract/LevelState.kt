package com.iti.linguaquest.features.onBoarding.presentation.contract.levelContract

import androidx.annotation.StringRes
import com.iti.linguaquest.R

enum class ProficiencyLevel(
    @StringRes val displayNameRes: Int,
    @StringRes val descriptionRes: Int
) {
    BEGINNER(R.string.level_beginner, R.string.level_beginner_description),
    INTERMEDIATE(R.string.level_intermediate, R.string.level_intermediate_description),
    ADVANCED(R.string.level_advanced, R.string.level_advanced_description)
}

data class LevelState(
    val selectedLevel: ProficiencyLevel = ProficiencyLevel.BEGINNER,
    val isContinueEnabled: Boolean = true
)
