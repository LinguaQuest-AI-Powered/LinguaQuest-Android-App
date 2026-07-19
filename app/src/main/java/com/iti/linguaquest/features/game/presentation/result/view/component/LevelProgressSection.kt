package com.iti.linguaquest.features.game.presentation.result.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun LevelProgressSection(
    currentLevel: Int,
    progressPercent: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.game_result_level_progress, currentLevel),
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 14.sp
            )
            Text(
                text = stringResource(R.string.game_result_percent_progress, (progressPercent * 100).toInt()),
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(
                    color = LinguaQuestTheme.colors.progressTrackRemainedColor,
                    shape = CircleShape
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressPercent)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}