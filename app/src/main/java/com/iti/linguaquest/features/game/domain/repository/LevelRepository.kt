package com.iti.linguaquest.features.game.domain.repository

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult

import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import java.io.File

interface LevelRepository {
    suspend fun startLevel(worldId: Int, order: Int): LinguaQuestResult<String, AppError>
    suspend fun changeWord(worldId: Int, order: Int): LinguaQuestResult<String, AppError>
    suspend fun verifyLevel(worldId: Int, order: Int, imageFile: File): LinguaQuestResult<VerifyLevelResult, AppError>
    suspend fun getHint(worldId: Int, order: Int): LinguaQuestResult<com.iti.linguaquest.features.game.domain.model.Hint, AppError>
}
