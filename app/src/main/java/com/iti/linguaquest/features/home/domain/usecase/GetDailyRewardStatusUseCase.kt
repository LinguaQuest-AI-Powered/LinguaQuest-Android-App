package com.iti.linguaquest.features.home.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardStatus
import com.iti.linguaquest.features.home.domain.repository.DailyRewardRepository
import javax.inject.Inject

class GetDailyRewardStatusUseCase @Inject constructor(
    private val repository: DailyRewardRepository
) {
    suspend operator fun invoke(): LinguaQuestResult<DailyRewardStatus, LinguaQuestDataError> =
        repository.getStatus()
}