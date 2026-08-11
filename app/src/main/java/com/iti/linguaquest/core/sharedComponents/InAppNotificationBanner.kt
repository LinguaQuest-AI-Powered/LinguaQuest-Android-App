package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R

@Composable
fun InAppNotificationBanner(
    title: String,
    message: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: String? = null
) {
    val notificationType = runCatching {
        if (type != null) NotificationType.valueOf(type) else NotificationType.SYSTEM
    }.getOrDefault(NotificationType.SYSTEM)

    NotificationCard(
        type = notificationType,
        title = title,
        body = message,
        modifier = modifier,
        onClick = onClick,
        prefixIcon = {
            val iconRes = when (notificationType) {
                NotificationType.ACHIEVEMENT_EARNED -> R.drawable.ic_cup
                NotificationType.STREAK_REMINDER -> R.drawable.ic_streak
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
        }
    )
}
