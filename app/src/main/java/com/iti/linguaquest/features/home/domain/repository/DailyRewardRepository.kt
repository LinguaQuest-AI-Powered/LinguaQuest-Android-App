package com.iti.linguaquest.features.home.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardClaimResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardStatus

interface DailyRewardRepository {
    suspend fun getStatus(): LinguaQuestResult<DailyRewardStatus, LinguaQuestDataError>
    suspend fun claim(): LinguaQuestResult<DailyRewardClaimResult, LinguaQuestDataError>
}