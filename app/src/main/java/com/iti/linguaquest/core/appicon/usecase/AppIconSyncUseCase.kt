package com.iti.linguaquest.core.appicon.usecase

import com.iti.linguaquest.core.appicon.domain.AppIconService
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import javax.inject.Inject

class AppIconSyncUseCase @Inject constructor(
    private val stateRepository: AppIconStateRepository,
    private val appIconService: AppIconService
) {
    suspend fun onHomeSnapshotLoaded(streakDays: Int) {
        stateRepository.observeHomeSnapshot(streakDays)
        appIconService.refresh()
    }

    suspend fun onProfileSnapshotLoaded(streakDays: Int, achievementCount: Int) {
        stateRepository.observeProfileSnapshot(streakDays, achievementCount)
        appIconService.refresh()
    }

    suspend fun onLessonCompleted() {
        appIconService.refresh()
    }
}
