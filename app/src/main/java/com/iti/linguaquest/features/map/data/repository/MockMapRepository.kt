package com.iti.linguaquest.features.map.data.repository

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.map.domain.model.MapLevel
import com.iti.linguaquest.features.map.domain.model.WorldMapDetail
import com.iti.linguaquest.features.map.domain.repository.MapRepository
import javax.inject.Inject

class MockMapRepository @Inject constructor() : MapRepository {
    override suspend fun getWorldMapDetail(worldId: Int): LinguaQuestResult<WorldMapDetail, AppError> {
        val totalLevels = if (worldId == 1) 8 else 12
        val completedLevels = if (worldId == 1) 2 else 0

        val levels = List(totalLevels) { index ->
            val levelNumber = index + 1
            val status = when {
                levelNumber <= completedLevels -> "COMPLETED"
                levelNumber == completedLevels + 1 -> "AVAILABLE"
                else -> "LOCKED"
            }
            MapLevel(
                id = levelNumber * 100,
                order = levelNumber,
                status = status,
                word = if (status == "COMPLETED") "Word \$levelNumber" else null
            )
        }

        val detail = WorldMapDetail(
            id = worldId,
            name = if (worldId == 1) "Park World" else "World \$worldId",
            difficulty = "EASY",
            levels = levels
        )

        return LinguaQuestResult.Success(detail)
    }
}
