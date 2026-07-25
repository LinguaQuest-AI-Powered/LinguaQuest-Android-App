package com.iti.linguaquest.features.profile.data.datasource.remote

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PasswordStatusDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PhotoResponseDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.ProfileDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdatePasswordRequestDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdateProfileRequestDto
import okhttp3.MultipartBody

interface EditProfileRemoteDataSource {

    suspend fun updateProfile(
        request: UpdateProfileRequestDto
    ): LinguaQuestResult<ProfileDto, LinguaQuestDataError>

    suspend fun uploadProfilePhoto(
        photo: MultipartBody.Part
    ): LinguaQuestResult<PhotoResponseDto, LinguaQuestDataError>

    suspend fun changePassword(
        request: UpdatePasswordRequestDto
    ): LinguaQuestResult<PasswordStatusDto, LinguaQuestDataError>
}