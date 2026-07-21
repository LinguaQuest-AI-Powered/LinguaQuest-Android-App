package com.iti.linguaquest.features.achivement.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
         Divider(
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
                    label = stringResource(com.iti.linguaquest.R.string.achievement_stat_earned),
                    valueColor = LinguaQuestTheme.colors.BrownText,
                    modifier = Modifier.weight(1f)
                )

                 Box(modifier = Modifier.width(1.dp).height(40.dp).background(dividerColor))

                StatItem(
                    value = inProgressCount.toString(),
                    label = stringResource(com.iti.linguaquest.R.string.achievement_stat_in_progress),
                    valueColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )

                 Box(modifier = Modifier.width(1.dp).height(40.dp).background(dividerColor))

                StatItem(
                    value = xpGained.toString(),
                    label = stringResource(com.iti.linguaquest.R.string.achievement_stat_xp_gained),
                    valueColor = LinguaQuestTheme.colors.BrownText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

             Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(LinguaQuestTheme.colors.AchievementButtonShadow, RoundedCornerShape(16.dp))
                    .padding(bottom = 6.dp)
                    .background(LinguaQuestTheme.colors.OrangeActive, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onClaimClick() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(LinguaQuestTheme.colors.whiteColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = LinguaQuestTheme.colors.OrangeActive,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(com.iti.linguaquest.R.string.achievement_claim_rewards),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.whiteColor
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, valueColor: Color, modifier: Modifier = Modifier) {
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
