package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.AppColors

@Composable
fun DailyRewardCard(
    modifier: Modifier = Modifier,
    currentDay: Int = 3,
    rewardAmount: Int = 50,
    onClaimClick: () -> Unit = {}
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            AppColors.DialogGradientTopRight.copy(alpha = 0.3f),
            Color.White,
            AppColors.DialogGradientBottomLeft.copy(alpha = 0.4f)
        )
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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

                DailyRewardTimeline(currentDay = currentDay)

                Spacer(modifier = Modifier.height(40.dp))

                RewardAmountBadge(rewardAmount = rewardAmount)

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
        DailyRewardCard(currentDay = 10, rewardAmount = 50)
    }
}
