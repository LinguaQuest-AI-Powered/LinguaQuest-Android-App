package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.features.notification.presentation.contract.NotificationIntent
import com.iti.linguaquest.features.notification.presentation.contract.NotificationState

@Composable
fun NotificationDialogs(
    state: NotificationState,
    onIntent: (NotificationIntent) -> Unit
) {
    if (state.showDeleteAllDialog) {
        AppDialog(
            title = stringResource(R.string.clear_all_notifications_title),
            message = stringResource(R.string.clear_all_notifications_message),
            imageRes = R.drawable.lingo_delete_notification,
            primaryButtonText = stringResource(R.string.notification_delete_all_confirm),
            onPrimaryClick = { onIntent(NotificationIntent.ConfirmDeleteAll) },
            secondaryButtonText = stringResource(R.string.notification_dismiss),
            onSecondaryClick = { onIntent(NotificationIntent.DismissDeleteAllDialog) },
            onDismissRequest = { onIntent(NotificationIntent.DismissDeleteAllDialog) },
            showCloseIcon = true
        )
    }

    state.notificationToDelete?.let { notificationId ->
        AppDialog(
            title = stringResource(R.string.delete_notification_title),
            message = stringResource(R.string.delete_notification_message),
            imageRes = R.drawable.lingo_delete_notification,
            primaryButtonText = stringResource(R.string.notification_delete_confirm),
            onPrimaryClick = { onIntent(NotificationIntent.ConfirmDeleteNotification(notificationId)) },
            secondaryButtonText = stringResource(R.string.notification_dismiss),
            onSecondaryClick = { onIntent(NotificationIntent.DismissDeleteNotificationDialog) },
            onDismissRequest = { onIntent(NotificationIntent.DismissDeleteNotificationDialog) },
            showCloseIcon = true
        )
    }
}