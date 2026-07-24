package com.iti.linguaquest.features.game.domain.usecase

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import java.io.File
import javax.inject.Inject

class VerifyLevelUseCase @Inject constructor(
    private val repository: LevelRepository
) {
    suspend operator fun invoke(
        worldId: Int,
        levelId: Int,
        imageFile: File
    ): LinguaQuestResult<VerifyLevelResult, AppError> {
        return repository.verifyLevel(worldId, levelId, imageFile)
    }
}
