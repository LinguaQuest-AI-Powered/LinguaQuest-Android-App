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

class VerifyDailyMissionUseCase @Inject constructor(
    private val repository: DailyMissionRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(imageUri: Uri, word: String): LinguaQuestResult<VerifyMissionResult, LinguaQuestDataError> {
        val file = getFileFromUri(imageUri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
        
        val wordRequestBody = word.toRequestBody("text/plain".toMediaTypeOrNull())
        
        return repository.verifyMission(imagePart, wordRequestBody)
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("daily_mission_", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }
}
