package com.iti.linguaquest.features.notification.data.datasource.remote

import com.iti.linguaquest.core.network.SuccessResponseDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationResponseDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.NotificationsPaginatedResponseDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.StatusResponseDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.UnreadCountResponseDto
import com.iti.linguaquest.features.notification.data.datasource.remote.dto.UnregisterDeviceRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationApiService {

    @POST("devices")
    suspend fun registerDeviceToken(
        @Body request: RegisterDeviceRequestDto
    ): SuccessResponseDto<NotificationResponseDto>

    @HTTP(method = "DELETE", path = "devices", hasBody = true)
    suspend fun unregisterDeviceToken(
        @Body request: UnregisterDeviceRequestDto
    ): SuccessResponseDto<NotificationResponseDto>

    @GET("notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<NotificationsPaginatedResponseDto>

    @GET("notifications/unread-count")
    suspend fun getUnreadCount(): UnreadCountResponseDto

    @DELETE("notifications")
    suspend fun deleteAllNotifications(): StatusResponseDto

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(
        @Path("id") id: Long
    ): StatusResponseDto

    @PATCH("notifications/{id}/read")
    suspend fun markAsRead(
        @Path("id") id: Long
    ): StatusResponseDto
}

