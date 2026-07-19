package com.iti.linguaquest.features.leaderboard.domain.repository


 import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
 import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
 import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope

interface LeaderboardRepository {

    suspend fun getLeaderboard(
        scope: LeaderboardScope = LeaderboardScope.GLOBAL,
        languageId: Int? = null,
        page: Int = 1,
        limit: Int = 20
    ): LinguaQuestResult<Leaderboard, LinguaQuestDataError>
}