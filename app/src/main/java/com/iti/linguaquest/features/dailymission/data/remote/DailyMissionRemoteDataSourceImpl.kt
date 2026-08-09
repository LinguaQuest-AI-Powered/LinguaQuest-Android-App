package com.iti.linguaquest.features.dailymission.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class DailyMissionRemoteDataSourceImpl @Inject constructor(
    private val apiService: DailyMissionApiService
) : DailyMissionRemoteDataSource {
    override suspend fun getDailyMission(): SuccessResponseDto<DailyMissionDto> {
        return apiService.getDailyMission()
    }

    override suspend fun verifyMission(
        image: MultipartBody.Part,
        word: RequestBody
    ): SuccessResponseDto<VerifyMissionDto> {
        return apiService.verifyMission(image, word)
    }
}
