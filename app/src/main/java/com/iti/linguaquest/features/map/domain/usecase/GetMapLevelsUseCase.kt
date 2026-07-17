package com.iti.linguaquest.features.map.domain.usecase

import com.iti.linguaquest.features.map.domain.model.MapLevel
import javax.inject.Inject

class GetMapLevelsUseCase @Inject constructor() {
    suspend operator fun invoke(totalLevels: Int, completedLevels: Int): List<MapLevel> {
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
