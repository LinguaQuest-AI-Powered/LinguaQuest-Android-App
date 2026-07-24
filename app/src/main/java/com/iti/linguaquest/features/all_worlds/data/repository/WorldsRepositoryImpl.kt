package com.iti.linguaquest.features.all_worlds.data.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.all_worlds.data.mapper.toDomain
import com.iti.linguaquest.features.all_worlds.data.remote.WorldsRemoteDataSource
import com.iti.linguaquest.features.all_worlds.domain.model.WorldDifficulty
import com.iti.linguaquest.features.all_worlds.domain.model.WorldsData
import com.iti.linguaquest.features.all_worlds.domain.repository.WorldsRepository
import javax.inject.Inject

class WorldsRepositoryImpl @Inject constructor(
    private val remoteDataSource: WorldsRemoteDataSource
) : WorldsRepository {

    override suspend fun getWorlds(
        languageId: Int?,
        difficulty: WorldDifficulty?
    ): LinguaQuestResult<WorldsData, LinguaQuestDataError> {
        val difficultyString = difficulty?.name
        val result = remoteDataSource.getWorlds(languageId, difficultyString)
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }
}
