package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.iti.linguaquest.core.sharedComponents.Card3DWrapper

@Composable
fun NotificationCard(
    notification: Notification,
    isDeleting: Boolean = false,
    modifier: Modifier = Modifier,
    onCardClick: (Rect) -> Unit = {},
    onDeleteClick: (Rect) -> Unit = {}
) {
    val type =
        runCatching { NotificationType.valueOf(notification.type) }.getOrDefault(NotificationType.SYSTEM)
    val cardBounds = remember(notification.id) { arrayOf(Rect.Zero) }

    val colors = getCardColors(isUnread = !notification.isRead)

    Card3DWrapper(
        backgroundColor = if (colors.bgTint != Color.Transparent) colors.bgTint else MaterialTheme.colorScheme.surface,
        borderColor = colors.borderColor,
        ledgeColor = colors.ledgeColor,
        borderWidth = colors.borderWidth,
        cornerRadius = 32.dp,
        modifier = modifier.onGloballyPositioned { coordinates ->
            cardBounds[0] = coordinates.boundsInRoot()
        },
        onClick = {
            onCardClick(cardBounds[0])
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconRes = when (type) {
                NotificationType.ACHIEVEMENT_EARNED -> R.drawable.ic_cup
                NotificationType.STREAK_REMINDER -> R.drawable.ic_streak
                NotificationType.DAILY_MISSION_AVAILABLE -> R.drawable.ic_prefix_mission
                else -> R.drawable.ic_bell_icon
            }

            val circleBgColor = when (type) {
                NotificationType.ACHIEVEMENT_EARNED -> MaterialTheme.colorScheme.tertiary
                else -> MaterialTheme.colorScheme.primary
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(circleBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    softWrap = true
                )
                Text(
                    text = notification.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    softWrap = true
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.createdAt,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            NotificationActions(
                isUnread = !notification.isRead,
                isDeleting = isDeleting,
                onDeleteClick = onDeleteClick
            )
        }
    }
}
