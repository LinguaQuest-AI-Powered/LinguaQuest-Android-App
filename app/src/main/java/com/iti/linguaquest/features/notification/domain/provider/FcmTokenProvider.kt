package com.iti.linguaquest.features.notification.domain.provider

interface FcmTokenProvider {
    suspend fun getToken(): String?
}
