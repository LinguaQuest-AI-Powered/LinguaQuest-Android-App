package com.iti.linguaquest.features.achivement.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.achivement.domain.model.AchievementStatus
import com.iti.linguaquest.features.achivement.presentation.view.model.AchievementItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementDetailBottomSheet(
    achievement: AchievementItem,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(LinguaQuestTheme.colors.IconBoxBackground),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.Close),
                            contentDescription = stringResource(id = R.string.achievement_detail_close),
                            tint = LinguaQuestTheme.colors.BrownText,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isEarned) LinguaQuestTheme.colors.AchievementCyanBackground
                        else LinguaQuestTheme.colors.IconBoxBackground
                    )
                    .border(
                        width = 2.dp,
                        color = if (achievement.isEarned) LinguaQuestTheme.colors.AchievementCyanText.copy(alpha = 0.3f)
                        else LinguaQuestTheme.colors.iconsColor.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                ImageWrapper(
                    model = achievement.icon,
                    contentDescription = achievement.title,
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val (statusText, statusBg, statusColor) = when (achievement.status) {
                AchievementStatus.EARNED -> Triple(
                    stringResource(R.string.achievement_detail_status_completed),
                    LinguaQuestTheme.colors.AchievementCyanBackground,
                    LinguaQuestTheme.colors.AchievementCyanText
                )
                AchievementStatus.IN_PROGRESS -> Triple(
                    stringResource(R.string.achievement_detail_status_in_progress),
                    LinguaQuestTheme.colors.OrangeActive.copy(alpha = 0.15f),
                    LinguaQuestTheme.colors.OrangeActive
                )
                AchievementStatus.LOCKED -> Triple(
                    stringResource(R.string.achievement_detail_status_locked),
                    LinguaQuestTheme.colors.IconBoxBackground,
                    LinguaQuestTheme.colors.iconsColor
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusBg)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = achievement.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = LinguaQuestTheme.colors.BrownText,
                textAlign = TextAlign.Center
            )

            if (achievement.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = achievement.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = LinguaQuestTheme.colors.iconsColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.achievement_detail_progress),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor
                    )
                    Text(
                        text = "${achievement.progressPercent}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (achievement.isEarned) LinguaQuestTheme.colors.AchievementCyanText
                        else LinguaQuestTheme.colors.OrangeActive
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (achievement.progressPercent / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = if (achievement.isEarned) LinguaQuestTheme.colors.AchievementCyanText
                    else LinguaQuestTheme.colors.OrangeActive,
                    trackColor = LinguaQuestTheme.colors.AchievementCardBorder
                )
            }

            if (achievement.xpReward > 0 || achievement.coinReward > 0) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (achievement.xpReward > 0) {
                        RewardPill(
                            iconRes = R.drawable.ic_xp,
                            text = stringResource(R.string.achievement_detail_xp_format, achievement.xpReward),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (achievement.coinReward > 0) {
                        RewardPill(
                            iconRes = R.drawable.ic_coin,
                            text = stringResource(R.string.achievement_detail_coins_format, achievement.coinReward),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (achievement.isEarned && !achievement.dateEarned.isNullOrBlank()) {
                    stringResource(R.string.achievement_detail_earned_on, achievement.dateEarned)
                } else {
                    stringResource(R.string.achievement_detail_keep_going)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppButton(
                text = stringResource(R.string.achievement_detail_close),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RewardPill(
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = LinguaQuestTheme.colors.BrownText
        )
    }
}
