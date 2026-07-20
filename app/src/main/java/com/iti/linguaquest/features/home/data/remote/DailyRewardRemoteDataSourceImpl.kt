package com.iti.linguaquest.features.home.data.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.remote.dto.ClaimDailyRewardResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.DailyRewardStatusDto
import javax.inject.Inject

class DailyRewardRemoteDataSourceImpl @Inject constructor(
    private val api: DailyRewardApiService
) : DailyRewardRemoteDataSource {

    override suspend fun getStatus(): LinguaQuestResult<DailyRewardStatusDto, LinguaQuestDataError> {
        val result = safeApiCall { api.getDailyRewardStatus() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun claim(): LinguaQuestResult<ClaimDailyRewardResponseDto, LinguaQuestDataError> {
        val result = safeApiCall { api.claimDailyReward() }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }
}