package com.iti.linguaquest.features.home.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.HomeSummaryDto
import retrofit2.http.GET

interface HomeApiService {
    @GET("home")
    suspend fun getHomeSummary(): SuccessResponseDto<HomeSummaryDto>
}