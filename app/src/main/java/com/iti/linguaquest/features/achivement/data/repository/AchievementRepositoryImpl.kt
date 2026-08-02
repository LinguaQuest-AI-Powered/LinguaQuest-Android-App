package com.iti.linguaquest.features.achivement.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.map
import com.iti.linguaquest.features.achivement.data.datasource.remote.AchievementRemoteDataSource
import com.iti.linguaquest.features.achivement.data.mapper.toDomain
import com.iti.linguaquest.features.achivement.domain.model.AchievementFilter
import com.iti.linguaquest.features.achivement.domain.model.AchievementsData
import com.iti.linguaquest.features.achivement.domain.repository.AchievementRepository
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor(
    private val remoteDataSource: AchievementRemoteDataSource
) : AchievementRepository {

    override suspend fun getAchievements(
        filter: AchievementFilter
    ): LinguaQuestResult<AchievementsData, LinguaQuestDataError> =
        remoteDataSource.getAchievements(filter.name)
            .map { it.toDomain() }
}
