package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.presentation.model.NotificationBadgeStyle

@Composable
fun Notification.getBadgeStyle(): NotificationBadgeStyle {
    val titleLower = title.lowercase()
    val isUnread = !isRead

    return when {
        type.equals("ACHIEVEMENT_EARNED", ignoreCase = true) ||
                listOf("trophy", "mastery", "streak").any { titleLower.contains(it) } -> {
            NotificationBadgeStyle(
                iconResId = R.drawable.ic_streak,
                badgeBg = MaterialTheme.colorScheme.primary,
                badgeShadow = LinguaQuestTheme.colors.ShadowOrange
            )
        }
        listOf("practice", "time").any { titleLower.contains(it) } -> {
            NotificationBadgeStyle(
                iconResId = R.drawable.ic_timer,
                badgeBg = MaterialTheme.colorScheme.primary,
                badgeShadow = LinguaQuestTheme.colors.ShadowOrange
            )
        }
        listOf("level", "world").any { titleLower.contains(it) } -> {
            NotificationBadgeStyle(
                iconResId = R.drawable.ic_profile_world,
                badgeBg = MaterialTheme.colorScheme.tertiary,
                badgeShadow = LinguaQuestTheme.colors.splashBottomRightColor
            )
        }
        else -> {
            NotificationBadgeStyle(
                iconResId = R.drawable.ic_bell_icon,
                badgeBg = if (isUnread) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                badgeShadow = if (isUnread) LinguaQuestTheme.colors.ShadowOrange else LinguaQuestTheme.colors.splashBottomRightColor
            )
        }
    }
}