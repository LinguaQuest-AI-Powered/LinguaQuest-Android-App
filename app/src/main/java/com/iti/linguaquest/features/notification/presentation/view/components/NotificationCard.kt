package com.iti.linguaquest.features.notification.presentation.view.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.features.notification.domain.model.Notification


@Composable
fun NotificationCard(
    notification: Notification,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val isUnread = !notification.isRead
    val badgeStyle = notification.getBadgeStyle()
    val cardColors = getCardColors(isUnread = isUnread)

    val cardInteractionSource = remember { MutableInteractionSource() }
    val isCardPressed by cardInteractionSource.collectIsPressedAsState()
    val cardOffset by animateDpAsState(
        targetValue = if (isCardPressed) 4.dp else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "cardPressOffset"
    )

    Box(modifier = modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 4.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(cardColors.ledgeColor)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .offset(y = cardOffset)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .background(cardColors.bgTint)
                .border(
                    width = cardColors.borderWidth,
                    color = cardColors.borderColor,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable(
                    interactionSource = cardInteractionSource,
                    indication = null,
                    onClick = onCardClick
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NotificationBadge(style = badgeStyle)

                Spacer(modifier = Modifier.width(16.dp))

                NotificationContent(
                    title = notification.title,
                    body = notification.body,
                    createdAt = notification.createdAt,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                NotificationActions(
                    isUnread = isUnread,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}
