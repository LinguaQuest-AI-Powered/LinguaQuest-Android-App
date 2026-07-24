package com.iti.linguaquest.features.game.domain.repository

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult

import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import java.io.File

interface LevelRepository {
    suspend fun startLevel(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError>
    suspend fun changeWord(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError>
    suspend fun verifyLevel(worldId: Int, levelId: Int, imageFile: File): LinguaQuestResult<VerifyLevelResult, AppError>
}
