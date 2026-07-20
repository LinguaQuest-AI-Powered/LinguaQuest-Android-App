package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardClaimResult
import com.iti.linguaquest.features.home.domain.repository.DailyRewardRepository
import javax.inject.Inject

class ClaimDailyRewardUseCase @Inject constructor(
    private val repository: DailyRewardRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<DailyRewardClaimResult, LinguaQuestDataError> =
        repository.claim()
}