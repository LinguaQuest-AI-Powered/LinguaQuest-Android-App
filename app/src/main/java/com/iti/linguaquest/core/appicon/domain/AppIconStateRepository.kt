package com.iti.linguaquest.core.appicon.domain

interface AppIconStateRepository {
    suspend fun snapshot(): AppIconSnapshot
    suspend fun markUserInteraction()
    suspend fun observeHomeSnapshot(streakDays: Int)
    suspend fun observeProfileSnapshot(streakDays: Int, achievementCount: Int)
    suspend fun consumeAchievements(upToCount: Int)
}
