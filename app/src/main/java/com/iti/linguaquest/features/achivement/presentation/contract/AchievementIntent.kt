package com.iti.linguaquest.features.achivement.presentation.contract

import com.iti.linguaquest.features.achivement.domain.model.AchievementFilter

sealed interface AchievementIntent {
    data object LoadAchievements : AchievementIntent
    data class ChangeFilter(val filter: AchievementFilter) : AchievementIntent
}
