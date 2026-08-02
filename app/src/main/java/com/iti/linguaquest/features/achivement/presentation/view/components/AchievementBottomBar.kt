package com.iti.linguaquest.features.achivement.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun AchievementBottomBar(
    earnedCount: Int,
    inProgressCount: Int,
    xpGained: Int,
    onClaimClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dividerColor = LinguaQuestTheme.colors.AchievementDivider

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        HorizontalDivider(
            color = dividerColor,
            thickness = 2.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(
                    value = earnedCount.toString(),
                    label = stringResource(R.string.achievement_stat_earned),
                    valueColor = LinguaQuestTheme.colors.BrownText,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(dividerColor)
                )

                StatItem(
                    value = inProgressCount.toString(),
                    label = stringResource(R.string.achievement_stat_in_progress),
                    valueColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(dividerColor)
                )

                StatItem(
                    value = xpGained.toString(),
                    label = stringResource(R.string.achievement_stat_xp_gained),
                    valueColor = LinguaQuestTheme.colors.BrownText,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.6f),
            letterSpacing = 1.sp
        )
    }
}
