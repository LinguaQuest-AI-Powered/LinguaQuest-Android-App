package com.iti.linguaquest.core.notification.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.core.notification.data.datasource.remote.dto.NotificationResponseDto
import com.iti.linguaquest.core.notification.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.iti.linguaquest.core.notification.data.datasource.remote.dto.UnregisterDeviceRequestDto
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface NotificationApiService {

    @POST("devices")
    suspend fun registerDeviceToken(
        @Body request: RegisterDeviceRequestDto
    ): SuccessResponseDto<NotificationResponseDto>

    @HTTP(method = "DELETE", path = "devices", hasBody = true)
    suspend fun unregisterDeviceToken(
        @Body request: UnregisterDeviceRequestDto
    ): SuccessResponseDto<NotificationResponseDto>
}
