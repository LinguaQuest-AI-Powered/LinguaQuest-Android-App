package com.iti.linguaquest.features.map.data.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.map.data.remote.dto.WorldMapDetailDto
import javax.inject.Inject

class MapRemoteDataSourceImpl @Inject constructor(
    private val api: MapApiService
) : MapRemoteDataSource {

    override suspend fun getWorldMapDetail(worldId: Int): LinguaQuestResult<WorldMapDetailDto, LinguaQuestDataError> {
        val result = safeApiCall { api.getWorldMapDetail(worldId) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }
}
