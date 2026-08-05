package com.iti.linguaquest.features.notification.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
 data class NotificationBadgeStyle(
    @DrawableRes val iconResId: Int,
    val badgeBg: Color,
    val badgeShadow: Color
)
