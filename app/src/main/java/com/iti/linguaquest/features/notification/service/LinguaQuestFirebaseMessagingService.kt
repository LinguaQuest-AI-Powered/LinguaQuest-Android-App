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

        val title = message.data["title"] ?: message.notification?.title ?: "LinguaQuest"
        val text = message.data["body"] ?: message.notification?.body ?: ""
        val type = message.data["type"] ?: "SYSTEM"
        
        notificationBannerController.showNotification(title, text, type)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
