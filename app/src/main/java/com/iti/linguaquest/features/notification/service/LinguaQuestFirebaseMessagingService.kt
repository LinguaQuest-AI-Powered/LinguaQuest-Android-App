package com.iti.linguaquest.features.notification.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.iti.linguaquest.core.sharedComponents.NotificationBannerController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LinguaQuestFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationBannerController: NotificationBannerController

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val text = message.notification?.body ?: message.data["data"] ?: message.data["message"] ?: "New Notification Received"
        
        notificationBannerController.showNotification(text)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
