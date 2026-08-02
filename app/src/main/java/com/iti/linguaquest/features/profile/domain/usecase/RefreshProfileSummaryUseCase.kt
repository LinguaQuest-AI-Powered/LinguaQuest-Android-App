package com.iti.linguaquest.features.profile.domain.usecase

import com.iti.linguaquest.core.appicon.domain.usecase.AppIconSyncUseCase
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class RefreshProfileSummaryUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val appIconSyncUseCase: AppIconSyncUseCase
) {
    suspend operator fun invoke(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        val result = profileRepository.refreshProfileSummary()
        if (result is LinguaQuestResult.Success) {
            profileRepository.cachedProfile.firstOrNull()?.let { snapshot ->
                appIconSyncUseCase.onProfileSnapshotLoaded(
                    streakDays = snapshot.stats.streakDays,
                    achievementCount = snapshot.achievementsSummary.earnedCount
                )
            }
        }
        return result
    }
}