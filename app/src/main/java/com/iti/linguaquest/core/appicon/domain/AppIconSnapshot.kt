package com.iti.linguaquest.core.appicon.domain

data class AppIconSnapshot(
    val lastUserInteractionAtMillis: Long? = null,
    val observedStreakDays: Int = 0,
    val observedAchievementCount: Int = 0,
    val consumedAchievementCount: Int = 0
)
