package com.iti.linguaquest.features.all_worlds.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.all_worlds.data.remote.dto.WorldsDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WorldsApiService {
    @GET("worlds")
    suspend fun getWorlds(
        @Query("languageId") languageId: Int? = null,
        @Query("difficulty") difficulty: String? = null
    ): SuccessResponseDto<WorldsDataDto>
}
