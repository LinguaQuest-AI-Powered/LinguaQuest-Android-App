package com.iti.linguaquest.features.profile.data.remote


import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.profile.data.remote.dto.ProfileSummaryDto
import retrofit2.http.GET

interface ProfileApiService {
    @GET("profile")
    suspend fun getProfileSummary(): SuccessResponseDto<ProfileSummaryDto>
}