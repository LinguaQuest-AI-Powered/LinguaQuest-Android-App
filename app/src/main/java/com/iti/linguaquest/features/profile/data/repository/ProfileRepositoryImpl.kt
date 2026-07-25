package com.iti.linguaquest.features.profile.data.repository

import com.iti.linguaquest.features.profile.domain.model.UserProfile
import com.iti.linguaquest.features.profile.domain.model.ProfilePhoto
import android.net.Uri
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.datasource.local.ProfileLocalDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.EditProfileRemoteDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.ProfileRemoteDataSource
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdatePasswordRequestDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdateProfileRequestDto
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MultipartBody
import javax.inject.Inject
import  com.iti.linguaquest.features.profile.data.mapper.toDomain
import com.iti.linguaquest.features.profile.domain.model.PasswordUpdateStatus
class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val localDataSource: ProfileLocalDataSource,
    private val eRemoteDataSource: EditProfileRemoteDataSource,
) : ProfileRepository {

    override val cachedProfile: Flow<ProfileSummary?> = localDataSource.cachedProfile
    override val cachedAvatarUrl: Flow<String?> = localDataSource.cachedAvatarUrl

    override suspend fun refreshProfileSummary(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = remoteDataSource.getProfileSummary()) {
            is LinguaQuestResult.Success -> {
                localDataSource.saveProfile(result.data)
                LinguaQuestResult.Success(Unit)
            }
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun uploadAvatar(imageUri: Uri): LinguaQuestResult<String, LinguaQuestDataError> {
        val result = remoteDataSource.uploadAvatar(imageUri)
        if (result is LinguaQuestResult.Success) {
            localDataSource.cachedProfile.firstOrNull()?.let { current ->
                localDataSource.saveProfile(current.copy(photoUrl = result.data))
            }
        }
        return result
    }


    override suspend fun updateProfile(username: String):
            LinguaQuestResult<UserProfile, LinguaQuestDataError>
    {
        val request = UpdateProfileRequestDto(username = username)
        val result = eRemoteDataSource.updateProfile(request)

        if (result is LinguaQuestResult.Success) {
             refreshProfileSummary()
        }

        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun uploadProfilePhoto(photo: MultipartBody.Part):
            LinguaQuestResult<ProfilePhoto, LinguaQuestDataError> {
        return when (val result = eRemoteDataSource.uploadProfilePhoto(photo)) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String):
            LinguaQuestResult<PasswordUpdateStatus, LinguaQuestDataError> {
        val request = UpdatePasswordRequestDto(
            oldPassword = oldPassword,
            newPassword = newPassword
        )

        return when (val result = eRemoteDataSource.changePassword(request)) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.toDomain())
            is LinguaQuestResult.Failure -> result
        }
    }
}