package com.iti.linguaquest.features.achivement.data.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.achivement.data.datasource.remote.dto.AchievementsResponseDataDto

interface AchievementRemoteDataSource {

    suspend fun getAchievements(
        status: String
    ): LinguaQuestResult<AchievementsResponseDataDto, LinguaQuestDataError>
}
