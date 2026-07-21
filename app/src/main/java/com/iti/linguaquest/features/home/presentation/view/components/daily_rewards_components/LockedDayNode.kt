package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R

@Composable
fun LockedDayNode() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(LinguaQuestTheme.colors.whiteColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(LinguaQuestTheme.colors.DailyRewardInactiveNodeBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.lock),
                contentDescription = stringResource(id = R.string.cd_locked),
                tint = LinguaQuestTheme.colors.DailyRewardInactiveNodeIcon,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
