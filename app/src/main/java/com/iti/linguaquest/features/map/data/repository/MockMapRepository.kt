package com.iti.linguaquest.features.map.data.repository

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.map.domain.model.MapLevel
import com.iti.linguaquest.features.map.domain.model.WorldMapDetail
import com.iti.linguaquest.features.map.domain.repository.MapRepository
import javax.inject.Inject

class MockMapRepository @Inject constructor() : MapRepository {
    override suspend fun getWorldMapDetail(worldId: Int): LinguaQuestResult<WorldMapDetail, AppError> {
        val totalLevels = 20
        val completedLevels = when (worldId) {
            10 -> 8 // Kitchen World
            11 -> 2 // City World
            else -> 0
        }

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
                word = if (status == "COMPLETED") "Word $levelNumber" else null
            )
        }

        val worldName = when (worldId) {
            10 -> "Kitchen World"
            11 -> "City World"
            12 -> "Park World"
            13 -> "School World"
            14 -> "Office World"
            15 -> "Gym World"
            else -> "Unknown World"
        }

        val detail = WorldMapDetail(
            id = worldId,
            name = worldName,
            difficulty = "EASY",
            levels = levels
        )

        return LinguaQuestResult.Success(detail)
    }
}
