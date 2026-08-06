package com.iti.linguaquest.features.notification.data.datasource.remote.dto

data class RegisterDeviceRequestDto(
    val token: String,
    val platform: String
)

data class UnregisterDeviceRequestDto(
    val token: String
)

data class NotificationResponseDto(
    val status: String
)
