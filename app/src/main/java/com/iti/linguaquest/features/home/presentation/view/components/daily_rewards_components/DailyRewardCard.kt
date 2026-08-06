package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sharedComponents.AppRewardsRow

@Composable
fun DailyRewardCard(
    modifier: Modifier = Modifier,
    currentDay: Int = 3,
    cycleLength: Int = 5,
    rewardAmount: Int = 50,
    rewardXp: Int? = null,
    onClaimClick: () -> Unit = {}
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            LinguaQuestTheme.colors.DialogGradientTopRight.copy(alpha = 0.3f),
            LinguaQuestTheme.colors.whiteColor,
            LinguaQuestTheme.colors.DialogGradientBottomLeft.copy(alpha = 0.4f)
        )
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = LinguaQuestTheme.colors.whiteColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DailyRewardHeader()

                Spacer(modifier = Modifier.height(48.dp))

                DailyRewardTimeline(currentDay = currentDay, cycleLength = cycleLength)

                Spacer(modifier = Modifier.height(40.dp))

                AppRewardsRow(
                    coinsAmount = rewardAmount,
                    xpAmount = rewardXp ?: 0
                )

                Spacer(modifier = Modifier.height(24.dp))

                ClaimRewardButton(onClaimClick = onClaimClick)
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
fun DailyRewardCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        DailyRewardCard(currentDay = 3, cycleLength = 5, rewardAmount = 50, rewardXp = 20)
    }
}
