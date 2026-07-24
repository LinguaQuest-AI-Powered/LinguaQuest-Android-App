package com.iti.linguaquest.features.game.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.game.data.remote.dto.StartLevelDto
import com.iti.linguaquest.features.game.data.remote.dto.VerifyLevelDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface LevelApiService {
    @POST("worlds/{worldId}/levels/{levelId}/start")
    suspend fun startLevel(
        @Path("worldId") worldId: Int,
        @Path("levelId") levelId: Int
    ): SuccessResponseDto<StartLevelDto>

    @PUT("worlds/{worldId}/levels/{levelId}/change-word")
    suspend fun changeWord(
        @Path("worldId") worldId: Int,
        @Path("levelId") levelId: Int
    ): SuccessResponseDto<StartLevelDto>

    @Multipart
    @POST("worlds/{worldId}/levels/{levelId}/verify")
    suspend fun verifyLevel(
        @Path("worldId") worldId: Int,
        @Path("levelId") levelId: Int,
        @Part image: MultipartBody.Part
    ): SuccessResponseDto<VerifyLevelDto>
}
