package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.features.notification.presentation.model.NotificationBadgeStyle

@Composable
fun Notification.getBadgeStyle(): NotificationBadgeStyle {
    val isUnread = !isRead

    return when (type) {
        "ACHIEVEMENT_EARNED", "STREAK_REMINDER" -> {
            NotificationBadgeStyle(
                iconResId = R.drawable.ic_streak,
                badgeBg = MaterialTheme.colorScheme.primary,
                badgeShadow = LinguaQuestTheme.colors.ShadowOrange
            )
        }
        "DAILY_REWARD_AVAILABLE", "DAILY_MISSION_AVAILABLE" -> {
            NotificationBadgeStyle(
                iconResId = R.drawable.ic_timer,
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