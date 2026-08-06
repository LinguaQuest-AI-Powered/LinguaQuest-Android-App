package com.iti.linguaquest.features.notification.presentation.contract

import com.iti.linguaquest.features.notification.domain.model.Notification

sealed interface NotificationIntent {
    data class NotificationClicked(val notification: Notification) : NotificationIntent
    data class DeleteNotificationClicked(val id: Long) : NotificationIntent
    data class ConfirmDeleteNotification(val id: Long) : NotificationIntent
    data object DismissDeleteNotificationDialog : NotificationIntent
    data object DeleteAllClicked : NotificationIntent
    data object ConfirmDeleteAll : NotificationIntent
    data object DismissDeleteAllDialog : NotificationIntent
}
