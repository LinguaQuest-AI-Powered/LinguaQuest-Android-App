package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun DailyRewardHeader() {
    ImageWrapper(
        model = R.drawable.lingo_reward,
        contentDescription = stringResource(id = R.string.cd_daily_reward),
        modifier = Modifier.size(160.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(id = R.string.daily_reward_title),
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = LinguaQuestTheme.colors.DailyRewardActiveText,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = stringResource(id = R.string.daily_reward_subtitle),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = LinguaQuestTheme.colors.DailyRewardSubtitleText,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    )
}