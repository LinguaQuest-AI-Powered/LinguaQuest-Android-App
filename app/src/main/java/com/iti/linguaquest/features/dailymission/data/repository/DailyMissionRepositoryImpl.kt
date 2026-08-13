package com.iti.linguaquest.features.dailymission.data.repository

import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.features.dailymission.data.remote.DailyMissionRemoteDataSource
import com.iti.linguaquest.features.dailymission.domain.model.DailyMission
import com.iti.linguaquest.features.dailymission.domain.model.VerifyMissionResult
import com.iti.linguaquest.features.dailymission.domain.repository.DailyMissionRepository
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import com.iti.linguaquest.features.game.data.remote.util.ImageCompressor
import javax.inject.Inject

class DailyMissionRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyMissionRemoteDataSource
) : DailyMissionRepository {
    private var cachedDailyMission: DailyMission? = null

    override suspend fun getDailyMission(forceRefresh: Boolean): LinguaQuestResult<DailyMission, LinguaQuestDataError> {
        if (!forceRefresh) {
            cachedDailyMission?.let {
                return LinguaQuestResult.Success(it)
            }
        }

        return when (val result = safeApiCall { remoteDataSource.getDailyMission() }) {
            is LinguaQuestResult.Success -> {
                val mission = DailyMission(word = result.data.data.word)
                cachedDailyMission = mission
                LinguaQuestResult.Success(mission)
            }
            is LinguaQuestResult.Failure -> LinguaQuestResult.Failure(result.error)
        }
    }

    override suspend fun verifyMission(
        imageFile: File,
        word: String
    ): LinguaQuestResult<VerifyMissionResult, LinguaQuestDataError> {
        val compressedFile = withContext(Dispatchers.IO) {
            ImageCompressor.compress(imageFile)
        }
            
        val requestFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", compressedFile.name, requestFile)

        val wordRequestBody = word.toRequestBody("text/plain".toMediaTypeOrNull())
        
        return when (val result = safeApiCall { remoteDataSource.verifyMission(imagePart, wordRequestBody) }) {
            is LinguaQuestResult.Success -> {
                LinguaQuestResult.Success(
                    VerifyMissionResult(
                        isMatch = result.data.data.isMatch,
                        xpEarned = result.data.data.xpEarned,
                        coinsEarned = result.data.data.coinsEarned
                    )
                )
            }
            is LinguaQuestResult.Failure -> LinguaQuestResult.Failure(result.error)
        }
    }
}
