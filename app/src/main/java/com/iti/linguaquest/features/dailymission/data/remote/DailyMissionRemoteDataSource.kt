package com.iti.linguaquest.features.dailymission.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import com.iti.linguaquest.core.network.SuccessResponseDto

interface DailyMissionRemoteDataSource {
    suspend fun getDailyMission(): SuccessResponseDto<DailyMissionDto>
    suspend fun verifyMission(
        image: MultipartBody.Part,
        word: RequestBody
    ): SuccessResponseDto<VerifyMissionDto>
}
