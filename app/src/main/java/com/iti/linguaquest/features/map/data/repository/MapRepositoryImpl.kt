package com.iti.linguaquest.features.map.data.repository

import android.util.Log
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.map.data.mapper.toDomain
import com.iti.linguaquest.features.map.data.remote.MapApiService
import com.iti.linguaquest.features.map.domain.model.WorldMapDetail
import com.iti.linguaquest.features.map.domain.repository.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val api: MapApiService
) : MapRepository {

    override suspend fun getWorldMapDetail(worldId: Int): LinguaQuestResult<WorldMapDetail, AppError> {
        val result = safeApiCall { api.getWorldMapDetail(worldId) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.toDomain())
            is LinguaQuestResult.Failure -> {
                Log.e("MapRepository", "Error fetching world map detail for worldId $worldId: ${result.error}")
                result
            }
        }
    }
}
