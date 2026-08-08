package com.iti.linguaquest.features.notification.presentation.contract

sealed interface NotificationEffect {
    data object RefreshNotifications : NotificationEffect
}
