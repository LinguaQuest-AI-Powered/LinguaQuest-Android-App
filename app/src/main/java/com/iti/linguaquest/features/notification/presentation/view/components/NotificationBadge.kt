package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.features.notification.presentation.model.NotificationBadgeStyle

@Composable
 fun NotificationBadge(
    style: NotificationBadgeStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 4.dp)
                .clip(CircleShape)
                .background(style.badgeShadow)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(CircleShape)
                .background(style.badgeBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = style.iconResId),
                contentDescription = stringResource(R.string.notifications_title),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}