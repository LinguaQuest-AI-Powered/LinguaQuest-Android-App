package com.iti.linguaquest.features.dailymission.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DailyMissionApiService {
    @GET("missions")
    suspend fun getDailyMission(): SuccessResponseDto<DailyMissionDto>

    @Multipart
    @POST("missions/verify")
    suspend fun verifyMission(
        @Part image: MultipartBody.Part,
        @Part("word") word: RequestBody
    ): SuccessResponseDto<VerifyMissionDto>
}
