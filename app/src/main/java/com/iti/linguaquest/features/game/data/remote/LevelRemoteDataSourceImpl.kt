package com.iti.linguaquest.features.game.data.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.game.data.remote.dto.HintDto
import com.iti.linguaquest.features.game.data.remote.dto.StartLevelDto
import com.iti.linguaquest.features.game.data.remote.dto.VerifyLevelDto
import com.iti.linguaquest.features.game.data.remote.util.ImageCompressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class LevelRemoteDataSourceImpl @Inject constructor(
    private val api: LevelApiService
) : LevelRemoteDataSource {

    override suspend fun startLevel(worldId: Int, order: Int): LinguaQuestResult<StartLevelDto, LinguaQuestDataError> {
        val result = safeApiCall { api.startLevel(worldId, order) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result

        }
    }

    override suspend fun changeWord(worldId: Int, order: Int): LinguaQuestResult<StartLevelDto, LinguaQuestDataError> {
        val result = safeApiCall { api.changeWord(worldId, order) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun verifyLevel(
        worldId: Int,
        order: Int,
        imageFile: File
    ): LinguaQuestResult<VerifyLevelDto, LinguaQuestDataError> {
        val compressedFile = withContext(Dispatchers.IO) {
            ImageCompressor.compress(imageFile)
        }
        val requestBody = compressedFile.asRequestBody("image/jpeg".toMediaType())
        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = compressedFile.name,
            body = requestBody
        )
        val result = safeApiCall {
            api.verifyLevel(
                worldId = worldId,
                order = order,
                image = imagePart
            )
        }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun getHint(
        worldId: Int,
        order: Int
    ): LinguaQuestResult<HintDto, LinguaQuestDataError> {
        val result = safeApiCall { api.getHint(worldId, order) }
        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }
}
