package com.iti.linguaquest.features.leaderboard.data.datasource.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.map
import com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource.LeaderboardRemoteDataSource
import com.iti.linguaquest.features.leaderboard.data.mapper.toDomain
import com.iti.linguaquest.features.leaderboard.domain.repository.LeaderboardRepository
import javax.inject.Inject
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope
import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard

class LeaderboardRepositoryImpl @Inject constructor(
    private val remoteDataSource: LeaderboardRemoteDataSource
) : LeaderboardRepository {

    override suspend fun getLeaderboard(
        scope: LeaderboardScope,
        languageId: Int?,
        page: Int,
        limit: Int
    ): LinguaQuestResult<Leaderboard, LinguaQuestDataError> =

        remoteDataSource.getLeaderboard(scope.name, languageId, page, limit)
            .map { it.toDomain() }
}