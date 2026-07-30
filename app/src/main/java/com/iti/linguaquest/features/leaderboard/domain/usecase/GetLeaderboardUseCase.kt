package com.iti.linguaquest.features.leaderboard.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope
import com.iti.linguaquest.features.leaderboard.domain.repository.LeaderboardRepository
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val repository: LeaderboardRepository
) {
    suspend operator fun invoke(
        scope: LeaderboardScope = LeaderboardScope.GLOBAL,
        languageId: Int? = null,
        page: Int = 0,
        limit: Int = 10
    ): LinguaQuestResult<Leaderboard, LinguaQuestDataError> =
        repository.getLeaderboard(scope, languageId, page, limit)
}