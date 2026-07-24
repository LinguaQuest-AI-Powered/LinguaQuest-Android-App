package com.iti.linguaquest.features.home.data.repository


import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.home.data.mapper.toDomain
import com.iti.linguaquest.features.home.data.dataSource.remote.DailyRewardRemoteDataSource
import com.iti.linguaquest.features.home.domain.model.DailyRewardClaimResult
import com.iti.linguaquest.features.home.domain.model.DailyRewardStatus
import com.iti.linguaquest.features.home.domain.repository.DailyRewardRepository
import javax.inject.Inject

class DailyRewardRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyRewardRemoteDataSource
) : DailyRewardRepository {

    override suspend fun getStatus(): LinguaQuestResult<DailyRewardStatus, LinguaQuestDataError> {
        return when (val result = remoteDataSource.getStatus()) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun claim(): LinguaQuestResult<DailyRewardClaimResult, LinguaQuestDataError> {
        return when (val result = remoteDataSource.claim()) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }
}