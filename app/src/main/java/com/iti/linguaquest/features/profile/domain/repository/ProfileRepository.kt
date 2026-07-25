package com.iti.linguaquest.features.profile.domain.repository


import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.model.PasswordUpdateStatus
import com.iti.linguaquest.features.profile.domain.model.ProfilePhoto
import com.iti.linguaquest.features.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ProfileRepository {

    val cachedProfile: Flow<ProfileSummary?>

    val cachedAvatarUrl: Flow<String?>

    suspend fun refreshProfileSummary(): LinguaQuestResult<Unit, LinguaQuestDataError>

    suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError>

    suspend fun updateProfile(
        username: String
    ): LinguaQuestResult<UserProfile, LinguaQuestDataError>

    suspend fun uploadProfilePhoto(
        photo: MultipartBody.Part
    ): LinguaQuestResult<ProfilePhoto, LinguaQuestDataError>

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): LinguaQuestResult<PasswordUpdateStatus, LinguaQuestDataError>
}