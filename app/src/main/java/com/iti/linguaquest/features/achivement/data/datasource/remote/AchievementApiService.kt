package com.iti.linguaquest.features.achivement.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.achivement.data.datasource.remote.dto.AchievementsResponseDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AchievementApiService {

    @GET("achievements")
    suspend fun getAchievements(
        @Query("status") status: String = "ALL"
    ): SuccessResponseDto<AchievementsResponseDataDto>
}
