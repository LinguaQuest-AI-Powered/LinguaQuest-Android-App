package com.iti.linguaquest.features.notification.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationItemDto(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("type") val type: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("body") val body: String? = null,
    @SerializedName("isRead") val isRead: Boolean? = null,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class NotificationsPaginatedResponseDto(
    @SerializedName("success") val success: Boolean? = true,
    @SerializedName("data") val data: NotificationsPageDataDto? = null
)

data class NotificationsPageDataDto(
    @SerializedName("notifications") val notifications: List<NotificationItemDto> = emptyList(),
    @SerializedName("totalElements") val totalElements: Long = 0,
    @SerializedName("page") val page: Int = 0,
    @SerializedName("size") val size: Int = 20
)

data class UnreadCountResponseDto(
    @SerializedName("success") val success: Boolean? = true,
    @SerializedName("data") val data: UnreadCountDataDto? = null
)

data class UnreadCountDataDto(
    @SerializedName("count") val count: Int = 0
)

data class StatusResponseDto(
    @SerializedName("success") val success: Boolean? = true,
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null
)
