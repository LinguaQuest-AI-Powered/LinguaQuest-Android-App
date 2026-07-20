package com.iti.linguaquest.features.home.data.remote



import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.ClaimDailyRewardResponseDto
import com.iti.linguaquest.features.home.data.remote.dto.DailyRewardStatusDto
import retrofit2.http.GET
import retrofit2.http.POST

interface DailyRewardApiService {
    @GET("daily-reward")
    suspend fun getDailyRewardStatus(): SuccessResponseDto<DailyRewardStatusDto>

    @POST("daily-reward/claim")
    suspend fun claimDailyReward(): SuccessResponseDto<ClaimDailyRewardResponseDto>
}