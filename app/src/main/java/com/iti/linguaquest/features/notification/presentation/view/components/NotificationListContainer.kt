package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.features.notification.domain.model.Notification

@Composable
fun NotificationListContainer(
    notifications: List<Notification>,
    isLoading: Boolean,
    isDeleting: Boolean,
    deletingNotificationId: Long?,
    onCardClick: (Notification, Rect) -> Unit,
    onDeleteClick: (Long, Rect) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading && notifications.isEmpty()) {
        LoadingView(modifier = modifier.fillMaxSize())
        return
    }

    if (notifications.isEmpty()) {
        NotificationEmptyState()
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = notifications,
            key = { it.id }
        ) { item ->
            NotificationCard(
                notification = item,
                isDeleting = isDeleting && (deletingNotificationId == null || deletingNotificationId == item.id),
                onCardClick = { anchor -> onCardClick(item, anchor) },
                onDeleteClick = { anchor -> onDeleteClick(item.id, anchor) }
            )
        }
    }
}
