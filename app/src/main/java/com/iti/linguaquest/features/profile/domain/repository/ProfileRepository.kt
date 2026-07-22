package com.iti.linguaquest.features.profile.domain.repository


import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getProfileSummary(): LinguaQuestResult<ProfileSummary, LinguaQuestDataError>
    suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError>
    val cachedAvatarUrl: Flow<String?>
}