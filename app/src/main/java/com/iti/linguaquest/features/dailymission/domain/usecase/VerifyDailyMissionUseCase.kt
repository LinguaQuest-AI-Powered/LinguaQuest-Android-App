package com.iti.linguaquest.features.dailymission.domain.usecase

import android.content.Context
import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.features.dailymission.domain.model.VerifyMissionResult
import com.iti.linguaquest.features.dailymission.domain.repository.DailyMissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

import com.iti.linguaquest.core.utils.toTempFile

class VerifyDailyMissionUseCase @Inject constructor(
    private val repository: DailyMissionRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(
        imageUri: Uri,
        word: String
    ): LinguaQuestResult<VerifyMissionResult, LinguaQuestDataError> {
        val file = imageUri.toTempFile(context, "daily_mission_") 
            ?: return LinguaQuestResult.Failure(LinguaQuestDataError.Local.DISK_FULL)
            
        val compressedFile = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            com.iti.linguaquest.features.game.data.remote.util.ImageCompressor.compress(file)
        }
            
        val requestFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", compressedFile.name, requestFile)

        val wordRequestBody = word.toRequestBody("text/plain".toMediaTypeOrNull())

        return repository.verifyMission(imagePart, wordRequestBody)
    }
}
