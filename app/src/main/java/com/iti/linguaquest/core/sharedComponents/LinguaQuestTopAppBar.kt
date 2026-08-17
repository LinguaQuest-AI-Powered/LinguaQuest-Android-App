package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.formatCompact
import com.iti.linguaquest.core.tutorial.presentation.tutorialTarget

@Composable
fun LinguaQuestTopAppBar(
    xp: Int,
    coins: Int,
    unreadCount: Int = 0,
    onBellClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val animatedXp by animateIntAsState(
        targetValue = xp,
        animationSpec = tween(durationMillis = 600),
        label = "xpAnim"
    )
    val animatedCoins by animateIntAsState(
        targetValue = coins,
        animationSpec = tween(durationMillis = 600),
        label = "livesAnim"
    )

    val barShape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = barShape,
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            )
            .clip(barShape)
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                shape = barShape
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.lingo_app_bar),
            contentDescription = stringResource(R.string.app_logo_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .background(MaterialTheme.colorScheme.background, CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .padding(3.dp)
                .clip(CircleShape)
        )

        Row(
            modifier = Modifier.padding(start = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StatChip(
                iconRes = R.drawable.ic_xp,
                value = animatedXp,
                textColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.tutorialTarget("tutorial_top_bar_xp")
            )

            StatChip(
                iconRes = R.drawable.ic_coin,
                value = animatedCoins,
                textColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.tutorialTarget("tutorial_top_bar_coins")
            )

            NotificationBell(
                unreadCount = unreadCount,
                onClick = onBellClick,
                modifier = Modifier.tutorialTarget("tutorial_top_bar_notifications")
            )
        }
    }
}

@Composable
private fun NotificationBell(
    unreadCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        BadgedBox(
            badge = {
                if (unreadCount > 0) {
                    val badgeText = if (unreadCount > 99) "+99" else "+$unreadCount"
                    Badge(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = LinguaQuestTheme.colors.whiteColor
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = stringResource(R.string.notifications_title),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun StatChip(
    iconRes: Int,
    value: Int,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(50),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = value.formatCompact(),
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
private fun LinguaQuestTopAppBarPreview() {
    LinguaQuestTheme {
        LinguaQuestTopAppBar(xp = 1250, coins = 45, unreadCount = 4)
    }
}
