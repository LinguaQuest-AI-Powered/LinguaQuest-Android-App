package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.NotificationType
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.core.sharedComponents.NotificationCard as SharedNotificationCard

@Composable
fun NotificationCard(
    notification: Notification,
    modifier: Modifier = Modifier,
    onCardClick: (Rect) -> Unit = {},
    onDeleteClick: (Rect) -> Unit = {}
) {
    val type =
        runCatching { NotificationType.valueOf(notification.type) }.getOrDefault(NotificationType.SYSTEM)
    var cardBounds by remember(notification.id) { mutableStateOf(Rect.Zero) }

    SharedNotificationCard(
        type = type,
        title = notification.title,
        body = notification.body,
        borderColor = if (!notification.isRead) MaterialTheme.colorScheme.primary else null,
        modifier = modifier.onGloballyPositioned { coordinates ->
            cardBounds = coordinates.boundsInRoot()
        },
        onClick = {
            onCardClick(cardBounds)
        },
        prefixIcon = {
            val iconRes = when (type) {
                NotificationType.ACHIEVEMENT_EARNED -> R.drawable.ic_cup
                NotificationType.STREAK_REMINDER -> R.drawable.ic_streak
                NotificationType.DAILY_MISSION_AVAILABLE -> R.drawable.ic_daily_mission
                else -> R.drawable.ic_bell_icon
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        },
        suffixIcon = {
            NotificationActions(
                isUnread = !notification.isRead,
                onDeleteClick = onDeleteClick
            )
        }
    )
}
