package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

enum class NotificationType {
    ACHIEVEMENT_EARNED,
    STREAK_REMINDER,
    DAILY_REWARD_AVAILABLE,
    DAILY_MISSION_AVAILABLE,
    SYSTEM
}

@Composable
fun NotificationCard(
    type: NotificationType,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    borderColor: Color? = null,
    prefixIcon: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.1f))
        )
    },
    suffixIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor = when (type) {
        NotificationType.ACHIEVEMENT_EARNED -> LinguaQuestTheme.colors.NotificationAchievementBg
        NotificationType.STREAK_REMINDER -> LinguaQuestTheme.colors.NotificationStreakBg
        NotificationType.DAILY_REWARD_AVAILABLE -> LinguaQuestTheme.colors.NotificationDailyRewardBg
        NotificationType.DAILY_MISSION_AVAILABLE -> LinguaQuestTheme.colors.NotificationDailyMissionBg
        NotificationType.SYSTEM -> MaterialTheme.colorScheme.surface
    }

    BaseNotificationCard(
        backgroundColor = backgroundColor,
        title = title,
        body = body,
        borderColor = borderColor,
        prefixIcon = prefixIcon,
        suffixIcon = suffixIcon,
        modifier = modifier,
        onClick = onClick
    )
}

@Composable
fun BaseNotificationCard(
    backgroundColor: Color,
    title: String,
    body: String,
    prefixIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color? = null,
    contentColor: Color = if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White,
    suffixIcon: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .then(
                if (borderColor != null) Modifier.border(
                    2.dp,
                    borderColor,
                    RoundedCornerShape(32.dp)
                )
                else Modifier
            )
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        prefixIcon()

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                softWrap = true
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor.copy(alpha = 0.8f),
                softWrap = true
            )
        }

        if (suffixIcon != null) {
            Spacer(modifier = Modifier.width(16.dp))
            suffixIcon()
        }
    }
}

@Preview
@Composable
fun PreviewNotificationCards() {
    LinguaQuestTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NotificationCard(
                type = NotificationType.ACHIEVEMENT_EARNED,
                title = "Achievement Unlocked!",
                body = "You have completed 10 lessons.",
                onClick = {}
            )

            NotificationCard(
                type = NotificationType.STREAK_REMINDER,
                title = "Keep it up!",
                body = "Don't lose your 5-day streak."
            )

            NotificationCard(
                type = NotificationType.DAILY_REWARD_AVAILABLE,
                title = "Daily Streak Bonus!",
                body = "Claim your daily reward!"
            )
        }
    }
}
