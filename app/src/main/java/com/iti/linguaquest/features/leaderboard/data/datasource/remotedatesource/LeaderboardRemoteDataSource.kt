package com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult

interface LeaderboardRemoteDataSource {
    suspend fun getLeaderboard(
        scope: String,
        languageId: Int?,
        page: Int,
        limit: Int
    ): LinguaQuestResult<LeaderboardDataDto, LinguaQuestDataError>
}