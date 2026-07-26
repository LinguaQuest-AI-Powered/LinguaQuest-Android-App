package com.iti.linguaquest.features.game.data.repository

import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.data.remote.LevelRemoteDataSource
import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import java.io.File
import javax.inject.Inject

class LevelRepositoryImpl @Inject constructor(
    private val remoteDataSource: LevelRemoteDataSource
) : LevelRepository {

    override suspend fun startLevel(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError> {
        return when (val result = remoteDataSource.startLevel(worldId, levelId)) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.targetWord.orEmpty())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun changeWord(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError> {
        return when (val result = remoteDataSource.changeWord(worldId, levelId)) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.targetWord.orEmpty())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun verifyLevel(
        worldId: Int,
        levelId: Int,
        imageFile: File
    ): LinguaQuestResult<VerifyLevelResult, AppError> {
        val result = remoteDataSource.verifyLevel(worldId, levelId, imageFile)
        return when (result) {
            is LinguaQuestResult.Success -> {
                val data = result.data
                LinguaQuestResult.Success(
                    VerifyLevelResult(
                        isMatch = data.isMatch ?: false,
                        xpEarned = data.xpEarned ?: 0,
                        coinsEarned = data.coinsEarned ?: 0,
                        level = data.level ?: 0,
                        levelProgressPercentage = data.levelProgressPercentage ?: 0
                    )
                )
            }
            is LinguaQuestResult.Failure -> result
        }
    }
}
