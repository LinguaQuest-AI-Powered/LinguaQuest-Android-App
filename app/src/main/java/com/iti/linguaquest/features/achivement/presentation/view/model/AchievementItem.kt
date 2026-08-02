package com.iti.linguaquest.features.achivement.presentation.view.model

import com.iti.linguaquest.features.achivement.domain.model.AchievementStatus

data class AchievementItem(
    val id: Int,
    val title: String,
    val dateEarned: String?,
    val icon: Any,
    val isEarned: Boolean,
    val description: String = "",
    val status: AchievementStatus = if (isEarned) AchievementStatus.EARNED else AchievementStatus.LOCKED,
    val progressPercent: Int = if (isEarned) 100 else 0,
    val targetValue: Int = 1,
    val xpReward: Int = 0,
    val coinReward: Int = 0
)
