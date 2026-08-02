package com.iti.linguaquest.features.achivement.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.achivement.domain.model.AchievementFilter
import com.iti.linguaquest.features.achivement.domain.model.AchievementsData

interface AchievementRepository {

    suspend fun getAchievements(
        filter: AchievementFilter
    ): LinguaQuestResult<AchievementsData, LinguaQuestDataError>
}
