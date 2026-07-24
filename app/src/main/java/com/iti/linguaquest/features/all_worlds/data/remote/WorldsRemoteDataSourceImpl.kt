package com.iti.linguaquest.features.all_worlds.data.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldsDataDto
import javax.inject.Inject

class WorldsRemoteDataSourceImpl @Inject constructor(
    private val api: WorldsApiService
) : WorldsRemoteDataSource {

    override suspend fun getWorlds(
        languageId: Int?,
        difficulty: String?
    ): LinguaQuestResult<WorldsDataDto, LinguaQuestDataError> {
        val result = safeApiCall { api.getWorlds(languageId, difficulty) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }
}
