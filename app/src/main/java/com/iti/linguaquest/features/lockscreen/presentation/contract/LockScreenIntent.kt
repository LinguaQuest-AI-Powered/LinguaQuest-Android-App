package com.iti.linguaquest.features.lockscreen.presentation.contract

sealed interface LockScreenIntent {
    data class ToggleFeatureClicked(val enabled: Boolean) : LockScreenIntent
    data class SyncNotificationPermission(val granted: Boolean) : LockScreenIntent
    data class NotificationPermissionResult(val granted: Boolean) : LockScreenIntent
    data object ConfirmEnableClicked : LockScreenIntent
    data object CancelEnableClicked : LockScreenIntent
    data object DisableClicked : LockScreenIntent
    data object RetryClicked : LockScreenIntent
    data object DismissErrorClicked : LockScreenIntent
    data object RefreshClicked : LockScreenIntent
}
