package com.iti.linguaquest.features.achivement.domain.usecase

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.achivement.domain.model.AchievementFilter
import com.iti.linguaquest.features.achivement.domain.model.AchievementsData
import com.iti.linguaquest.features.achivement.domain.repository.AchievementRepository
import javax.inject.Inject

class GetAchievementsUseCase @Inject constructor(
    private val repository: AchievementRepository
) {

    suspend operator fun invoke(
        filter: AchievementFilter = AchievementFilter.ALL
    ): LinguaQuestResult<AchievementsData, LinguaQuestDataError> =
        repository.getAchievements(filter)
}
