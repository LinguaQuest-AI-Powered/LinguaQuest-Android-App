package com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestResult
import jakarta.inject.Inject
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.map

class LeaderboardRemoteDataSourceImpl @Inject constructor(
    private val apiService: LeaderboardApiService
) : LeaderboardRemoteDataSource {

    override suspend fun getLeaderboard(
        scope: String,
        languageId: Int?,
        page: Int,
        limit: Int
    ): LinguaQuestResult<LeaderboardDataDto, LinguaQuestDataError> =

        safeApiCall {
            apiService.getLeaderboard(scope, languageId, page, limit)
        }.map { it.data }
}
