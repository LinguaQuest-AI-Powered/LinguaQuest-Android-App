package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.notification.presentation.model.NotificationCardColors


@Composable
 fun getCardColors(isUnread: Boolean): NotificationCardColors {
    return if (isUnread) {
        NotificationCardColors(
            ledgeColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            bgTint = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            borderWidth = 2.dp
        )
    } else {
        NotificationCardColors(
            ledgeColor = LinguaQuestTheme.colors.AchievementCardBorder.copy(alpha = 0.6f),
            borderColor = LinguaQuestTheme.colors.AchievementCardBorder,
            bgTint = Color.Transparent,
            borderWidth = 1.5.dp
        )
    }
}