package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun ScoreCircle(rating: Int, isPassed: Boolean) {
    val progress = rating / 10f
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(150.dp),
            color = if (isPassed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary,
            trackColor = AppColors.DialogOutline,
            strokeWidth = 8.dp
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$rating/10",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = if (isPassed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
            )
            Text(stringResource(R.string.voice_result_score), style = MaterialTheme.typography.labelMedium, color = LinguaQuestTheme.colors.blackColor)
        }
    }
}