package com.iti.linguaquest.features.leaderboard.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.leaderboard.domain.model.Leaderboard
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardScope
import com.iti.linguaquest.features.leaderboard.domain.repository.LeaderboardRepository
import jakarta.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val repository: LeaderboardRepository
) {
    suspend operator fun invoke(
        scope: LeaderboardScope = LeaderboardScope.GLOBAL,
        languageId: Int? = null,
        page: Int = 1,
        limit: Int = 20
    ): LinguaQuestResult<Leaderboard, LinguaQuestDataError> =
        repository.getLeaderboard(scope, languageId, page, limit)
}