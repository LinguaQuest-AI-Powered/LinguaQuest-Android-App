package com.iti.linguaquest.features.profile.datasource.remote


import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.profile.datasource.remote.dto.ProfileSummaryDto
import retrofit2.http.GET

interface ProfileApiService {
    @GET("profile")
    suspend fun getProfileSummary(): SuccessResponseDto<ProfileSummaryDto>
}