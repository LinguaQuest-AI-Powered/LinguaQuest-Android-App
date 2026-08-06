package com.iti.linguaquest.core.sharedComponents

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationBannerController @Inject constructor() {
    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage: StateFlow<String?> = _notificationMessage

    fun showNotification(message: String?) {
        _notificationMessage.value = message
    }
}
