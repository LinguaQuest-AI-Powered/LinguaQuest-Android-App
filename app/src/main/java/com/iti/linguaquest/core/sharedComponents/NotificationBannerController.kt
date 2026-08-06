package com.iti.linguaquest.core.sharedComponents

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class NotificationBannerState(
    val title: String,
    val message: String,
    val type: String? = null
)

@Singleton
class NotificationBannerController @Inject constructor() {
    private val _notificationMessage = MutableStateFlow<NotificationBannerState?>(null)
    val notificationMessage: StateFlow<NotificationBannerState?> = _notificationMessage

    fun showNotification(title: String, message: String, type: String? = null) {
        _notificationMessage.value = NotificationBannerState(title, message, type)
    }
    
    fun hideNotification() {
        _notificationMessage.value = null
    }
}
