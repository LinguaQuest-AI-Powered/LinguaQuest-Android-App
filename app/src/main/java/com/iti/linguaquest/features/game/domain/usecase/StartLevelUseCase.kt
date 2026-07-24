package com.iti.linguaquest.features.game.domain.usecase

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import javax.inject.Inject

class StartLevelUseCase @Inject constructor(
    private val repository: LevelRepository
) {
    suspend operator fun invoke(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError> {
        return repository.startLevel(worldId, levelId)
    }
}
