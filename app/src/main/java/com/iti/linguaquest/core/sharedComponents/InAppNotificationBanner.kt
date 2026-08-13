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
    var notificationType = type?.let { t ->
        NotificationType.values().firstOrNull { 
            it.name.contains(t, ignoreCase = true) || t.contains(it.name, ignoreCase = true) 
        }
    } ?: NotificationType.SYSTEM

    if (notificationType == NotificationType.SYSTEM) {
        val lowerTitle = title.lowercase()
        notificationType = when {
            lowerTitle.contains("trophy") || lowerTitle.contains("achievement") -> NotificationType.ACHIEVEMENT_EARNED
            lowerTitle.contains("streak") -> NotificationType.STREAK_REMINDER
            lowerTitle.contains("mission") -> NotificationType.DAILY_MISSION_AVAILABLE
            lowerTitle.contains("reward") || lowerTitle.contains("bonus") -> NotificationType.DAILY_REWARD_AVAILABLE
            else -> NotificationType.SYSTEM
        }
    }

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
                NotificationType.DAILY_MISSION_AVAILABLE -> R.drawable.ic_prefix_mission
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
        suffixIcon = if (notificationType != NotificationType.SYSTEM) {
            {
                val suffixIconRes = when (notificationType) {
                    NotificationType.ACHIEVEMENT_EARNED -> R.drawable.ic_check
                    NotificationType.STREAK_REMINDER -> R.drawable.ic_spark
                    NotificationType.DAILY_MISSION_AVAILABLE -> R.drawable.ic_new_mission
                    NotificationType.DAILY_REWARD_AVAILABLE -> R.drawable.ic_daily_bouns
                    else -> R.drawable.ic_bell_icon
                }
                Icon(
                    painter = painterResource(id = suffixIconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else null
    )
}
