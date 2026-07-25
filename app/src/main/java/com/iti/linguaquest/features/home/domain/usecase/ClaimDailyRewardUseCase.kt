package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.appicon.usecase.AppIconSyncUseCase
import com.iti.linguaquest.features.home.domain.model.DailyRewardClaimResult
import com.iti.linguaquest.features.home.domain.repository.DailyRewardRepository
import javax.inject.Inject

class ClaimDailyRewardUseCase @Inject constructor(
    private val repository: DailyRewardRepository,
    private val appIconSyncUseCase: AppIconSyncUseCase
) {
    suspend operator fun invoke(): LinguaQuestResult<DailyRewardClaimResult, LinguaQuestDataError> {
        val result = repository.claim()
        if (result is LinguaQuestResult.Success) {
            appIconSyncUseCase.onHomeSnapshotLoaded(result.data.newStreakDays)
        }
        return result
    }
}
