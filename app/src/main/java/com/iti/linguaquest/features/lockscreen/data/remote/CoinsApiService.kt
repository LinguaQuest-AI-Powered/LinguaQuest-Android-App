package com.iti.linguaquest.features.lockscreen.data.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.lockscreen.data.remote.dto.DeductCoinsRequestDto
import com.iti.linguaquest.features.lockscreen.data.remote.dto.DeductCoinsResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CoinsApiService {
    @POST("users/deduct-coins")
    suspend fun deductCoins(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Body request: DeductCoinsRequestDto
    ): SuccessResponseDto<DeductCoinsResponseDto>
}
