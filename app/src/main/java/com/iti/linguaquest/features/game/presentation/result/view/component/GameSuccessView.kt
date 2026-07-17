package com.iti.linguaquest.features.game.presentation.result.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun GameSuccessView(
    xpGained: Int,
    coinsGained: Int,
    currentLevel: Int,
    progressPercent: Float,
    onNextLevelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppMascotGradientBox(
            imageRes = R.drawable.lingo_success,
            mascotSize = 160.dp,
            mascotOverlapHeight = 70.dp
        ) {

            // --- Title & Subtitle ---
            Text(
                text = "Perfect!",
                style = AppTextStyles.ScreenTitle,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp,
                color = LinguaQuestTheme.colors.BrownText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You found it!",
                style = AppTextStyles.DialogMessage,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Rewards Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RewardPill(
                    iconRes = R.drawable.ic_xp,
                    amount = "+$xpGained",
                    label = "XP",
                    modifier = Modifier.weight(1f)
                )
                RewardPill(
                    iconRes = R.drawable.ic_coin,
                    amount = "+$coinsGained",
                    label = "COINS",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Level Progress Section ---
            LevelProgressSection(
                currentLevel = currentLevel,
                progressPercent = progressPercent
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- Action Button ---
            AppButton(
                text = "Next Level",
                onClick = onNextLevelClick
            )
        }
    }
}