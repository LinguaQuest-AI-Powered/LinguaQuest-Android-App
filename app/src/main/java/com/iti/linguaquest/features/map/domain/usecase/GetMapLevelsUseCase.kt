package com.iti.linguaquest.features.map.domain.usecase

import com.iti.linguaquest.features.map.domain.model.MapLevel
import javax.inject.Inject

class GetMapLevelsUseCase @Inject constructor() {
    suspend operator fun invoke(worldId: Int): List<MapLevel> {
        // Mock data logic based on worldId
        val totalLevels = if (worldId == 1) 8 else 12
        val completedLevels = if (worldId == 1) 2 else 0

        return List(totalLevels) { index ->
            val levelNumber = index + 1
            MapLevel(
                levelNumber = levelNumber,
                isCompleted = levelNumber <= completedLevels,
                isCurrent = levelNumber == completedLevels + 1,
                stars = if (levelNumber <= completedLevels) (1..3).random() else 0
            )
        }
    }
}
