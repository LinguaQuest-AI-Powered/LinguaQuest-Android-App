package com.iti.linguaquest.features.map.data.repository

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.map.data.mapper.toDomain
import com.iti.linguaquest.features.map.data.remote.MapRemoteDataSource
import com.iti.linguaquest.features.map.domain.model.WorldMapDetail
import com.iti.linguaquest.features.map.domain.repository.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val remoteDataSource: MapRemoteDataSource
) : MapRepository {

    override suspend fun getWorldMapDetail(worldId: Int): LinguaQuestResult<WorldMapDetail, AppError> {
        return when (val result = remoteDataSource.getWorldMapDetail(worldId)) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }
}
