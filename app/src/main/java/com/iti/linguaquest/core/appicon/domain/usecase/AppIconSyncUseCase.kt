package com.iti.linguaquest.core.appicon.domain.usecase

import com.iti.linguaquest.core.appicon.domain.AppIconService
import javax.inject.Inject

class AppIconSyncUseCase @Inject constructor(
    private val appIconService: AppIconService
) {
    suspend fun onHomeSnapshotLoaded(streakDays: Int) {
        appIconService.saveHomeSnapshot(streakDays)
    }

    suspend fun onProfileSnapshotLoaded(streakDays: Int, achievementCount: Int) {
        appIconService.saveProfileSnapshot(streakDays, achievementCount)
    }

    suspend fun onLessonCompleted() {
        appIconService.refresh()
    }
}
