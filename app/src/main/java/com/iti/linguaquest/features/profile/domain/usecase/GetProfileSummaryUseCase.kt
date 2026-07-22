package com.iti.linguaquest.features.profile.domain.usecase


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.appicon.usecase.AppIconSyncUseCase
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileSummaryUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val appIconSyncUseCase: AppIconSyncUseCase
) {
    suspend operator fun invoke(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError> {
        val result = profileRepository.getProfileSummary()
        if (result is LinguaQuestResult.Success) {
            appIconSyncUseCase.onProfileSnapshotLoaded(
                streakDays = result.data.stats.streakDays,
                achievementCount = result.data.achievementsSummary.earnedCount
            )
        }
        return result
    }
}
