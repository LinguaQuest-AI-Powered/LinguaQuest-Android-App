package com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource

import com.iti.linguaquest.core.network.SuccessResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface LeaderboardApiService {

     @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("scope") scope: String = "GLOBAL",
        @Query("languageId") languageId: Int? = null,
        @Query("page") page: Int = 0,
        @Query("limit") limit: Int = 20
    ): SuccessResponseDto<LeaderboardDataDto>
}
