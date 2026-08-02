package com.iti.linguaquest.features.achivement.data.datasource.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.map
import com.iti.linguaquest.features.achivement.data.datasource.remote.dto.AchievementsResponseDataDto
import javax.inject.Inject

class AchievementRemoteDataSourceImpl @Inject constructor(
    private val apiService: AchievementApiService
) : AchievementRemoteDataSource {

    override suspend fun getAchievements(
        status: String
    ): LinguaQuestResult<AchievementsResponseDataDto, LinguaQuestDataError> =
        safeApiCall {
            apiService.getAchievements(status)
        }.map { it.data }
}
