package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun RewardAmountBadge(
    modifier: Modifier = Modifier,
    rewardAmount: Int,
    rewardXp: Int? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RewardBadgeItem(
            iconRes = R.drawable.ic_doller,
            contentDescriptionRes = R.string.cd_coin,
            text = stringResource(id = R.string.daily_reward_coins_format, rewardAmount)
        )

        if (rewardXp != null && rewardXp > 0) {
            RewardBadgeItem(
                iconRes = R.drawable.ic_xp,
                contentDescriptionRes = R.string.cd_xp,
                text = stringResource(id = R.string.daily_reward_xp_format, rewardXp)
            )
        }
    }
}

@Composable
private fun RewardBadgeItem(
    modifier: Modifier = Modifier,
    iconRes: Int,
    contentDescriptionRes: Int,
    text: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LinguaQuestTheme.colors.DailyRewardBadgeBg)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(id = contentDescriptionRes),
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.DailyRewardBadgeText
                )
            )
        }
    }
}
