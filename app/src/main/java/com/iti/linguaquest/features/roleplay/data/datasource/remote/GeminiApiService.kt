package com.iti.linguaquest.features.roleplay.data.datasource.remote

import com.iti.linguaquest.core.network.NoAuth
import com.iti.linguaquest.features.roleplay.data.datasource.remote.model.GeminiRequestDto
import com.iti.linguaquest.features.roleplay.data.datasource.remote.model.GeminiResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiApiService {

    @NoAuth
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequestDto
    ): Response<GeminiResponseDto>
}
