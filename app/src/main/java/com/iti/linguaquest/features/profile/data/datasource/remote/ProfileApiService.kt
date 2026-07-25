package com.iti.linguaquest.features.profile.data.datasource.remote


import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.ProfileSummaryDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.UploadAvatarResponseDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileApiService {
    @GET("profile")
    suspend fun getProfileSummary(): SuccessResponseDto<ProfileSummaryDto>

    @Multipart
    @POST("profile/photo")
    suspend fun uploadAvatarPhoto(
        @Part photo: MultipartBody.Part
    ): SuccessResponseDto<UploadAvatarResponseDto>
}