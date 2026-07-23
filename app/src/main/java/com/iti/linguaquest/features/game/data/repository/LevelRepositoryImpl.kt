package com.iti.linguaquest.features.game.data.repository

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.AppError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.data.remote.LevelApiService
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import javax.inject.Inject

import com.iti.linguaquest.features.game.domain.model.VerifyLevelResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class LevelRepositoryImpl @Inject constructor(
    private val api: LevelApiService
) : LevelRepository {

    override suspend fun startLevel(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError> {
        val result = safeApiCall { api.startLevel(worldId, levelId) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.targetWord.orEmpty())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun changeWord(worldId: Int, levelId: Int): LinguaQuestResult<String, AppError> {
        val result = safeApiCall { api.changeWord(worldId, levelId) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data.targetWord.orEmpty())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun verifyLevel(
        worldId: Int,
        levelId: Int,
        imageFile: File
    ): LinguaQuestResult<VerifyLevelResult, AppError> {
        val requestBody = imageFile.asRequestBody("image/*".toMediaType())
        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = imageFile.name,
            body = requestBody
        )
        val result = safeApiCall {
            api.verifyLevel(
                worldId = worldId,
                levelId = levelId,
                image = imagePart
            )
        }
        return when (result) {
            is LinguaQuestResult.Success -> {
                val data = result.data.data
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
