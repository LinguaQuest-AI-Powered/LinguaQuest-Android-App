package com.iti.linguaquest.features.notification.presentation.model


import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Immutable
 data class NotificationCardColors(
    val ledgeColor: Color,
    val borderColor: Color,
    val bgTint: Color,
    val borderWidth: Dp
)
