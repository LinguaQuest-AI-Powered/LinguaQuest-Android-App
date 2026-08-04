package com.iti.linguaquest.core.notification.data.helper

import com.google.firebase.messaging.FirebaseMessaging
import com.iti.linguaquest.core.notification.domain.provider.FcmTokenProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor() : FcmTokenProvider {

    override suspend fun getToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }
    }
}
