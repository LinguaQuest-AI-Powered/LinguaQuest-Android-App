package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun StarRatingRow(
    stars: Int,
    modifier: Modifier = Modifier,
    starSize: Dp = 32.dp,
    spacing: Dp = 6.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..3) {
            val isEarned = i <= stars
            Icon(
                imageVector = if (isEarned) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (isEarned) LinguaQuestTheme.colors.LeaderboardGold else LinguaQuestTheme.colors.progressTrackRemainedColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StarRatingRowPreview() {
    LinguaQuestTheme {
        StarRatingRow(stars = 2)
    }
}
