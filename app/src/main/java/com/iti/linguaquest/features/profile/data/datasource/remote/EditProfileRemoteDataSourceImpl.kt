package com.iti.linguaquest.features.profile.data.datasource.remote

import com.iti.linguaquest.core.network.safeApiCall
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdateProfileRequestDto
import okhttp3.MultipartBody
import javax.inject.Inject
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PhotoResponseDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.ProfileDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdatePasswordRequestDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PasswordStatusDto

class EditProfileRemoteDataSourceImpl @Inject constructor(
    private val api: EditProfileApiService
) : EditProfileRemoteDataSource {

    override suspend fun updateProfile(
        request: UpdateProfileRequestDto
    ): LinguaQuestResult<ProfileDto, LinguaQuestDataError> {

        val result = safeApiCall { api.updateProfile(request) }

        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun uploadProfilePhoto(
        photo: MultipartBody.Part
    ): LinguaQuestResult<PhotoResponseDto, LinguaQuestDataError> {

        val result = safeApiCall { api.uploadProfilePhoto(photo) }

        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }

    override suspend fun changePassword(
        request: UpdatePasswordRequestDto
    ): LinguaQuestResult<PasswordStatusDto, LinguaQuestDataError> {

        val result = safeApiCall { api.changePassword(request) }

        return when (result) {
            is LinguaQuestResult.Success -> LinguaQuestResult.Success(result.data.data)
            is LinguaQuestResult.Failure -> result
        }
    }
}