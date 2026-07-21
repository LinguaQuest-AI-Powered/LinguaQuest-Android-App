package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R

@Composable
fun DifficultyChip(difficulty: WorldDifficulty, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(difficulty.badgeColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        val stringResId = when (difficulty) {
            WorldDifficulty.EASY -> R.string.easy
            WorldDifficulty.MEDIUM -> R.string.medium
            WorldDifficulty.HARD -> R.string.hard
        }
        Text(
            text = stringResource(stringResId),
            color = LinguaQuestTheme.colors.whiteColor,
            style = AppTextStyles.Caption.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
            )
        )
    }
}