package com.iti.linguaquest.features.profile.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PasswordStatusDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PhotoResponseDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.ProfileDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdatePasswordRequestDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UpdateProfileRequestDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface EditProfileApiService {

    @PATCH("profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): SuccessResponseDto<ProfileDto>

    @Multipart
    @POST("profile/photo")
    suspend fun uploadProfilePhoto(
        @Part photo: MultipartBody.Part
    ): SuccessResponseDto<PhotoResponseDto>

    @PATCH("profile/password")
    suspend fun changePassword(
        @Body request: UpdatePasswordRequestDto
    ): SuccessResponseDto<PasswordStatusDto>
}