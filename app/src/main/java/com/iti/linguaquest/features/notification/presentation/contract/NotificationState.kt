package com.iti.linguaquest.features.notification.presentation.contract

data class NotificationState(
    val isLoading: Boolean = true,
    val showDeleteAllDialog: Boolean = false,
    val notificationToDelete: Long? = null,
    val isDeleting: Boolean = false,
    val isOnline: Boolean = true
)
