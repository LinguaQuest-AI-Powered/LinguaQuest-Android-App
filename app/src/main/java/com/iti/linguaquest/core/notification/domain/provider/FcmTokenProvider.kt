package com.iti.linguaquest.core.notification.domain.provider

interface FcmTokenProvider {
    suspend fun getToken(): String?
}
