package com.iti.linguaquest.features.notification.presentation.contract

data class NotificationState(
    val deletedNotificationIds: Set<Long> = emptySet(),
    val readNotificationIds: Set<Long> = emptySet(),
    val showDeleteAllDialog: Boolean = false,
    val notificationToDelete: Long? = null,
    val isDeleting: Boolean = false
)
