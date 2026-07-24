package com.iti.linguaquest.features.profile.domain.repository


import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    val cachedProfile: Flow<ProfileSummary?>

    val cachedAvatarUrl: Flow<String?>

    suspend fun refreshProfileSummary(): LinguaQuestResult<Unit, LinguaQuestDataError>

    suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError>
}