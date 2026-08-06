package com.iti.linguaquest.features.notification.data.helper

import com.google.firebase.messaging.FirebaseMessaging
import com.iti.linguaquest.features.notification.domain.provider.FcmTokenProvider
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor() : FcmTokenProvider {

    override suspend fun getToken(): String? {
        return runCatching {
            FirebaseMessaging.getInstance().token.await()
        }.getOrNull()
    }
}
