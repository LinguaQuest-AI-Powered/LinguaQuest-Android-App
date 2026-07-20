package com.iti.linguaquest.features.home.data.remote


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.remote.dto.ClaimDailyRewardResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.DailyRewardStatusDto

interface DailyRewardRemoteDataSource {
    suspend fun getStatus(): LinguaQuestResult<DailyRewardStatusDto, LinguaQuestDataError>
    suspend fun claim(): LinguaQuestResult<ClaimDailyRewardResponseDto, LinguaQuestDataError>
}