package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun MindReaderProgressBar(
    currentQuestion: Int,
    maxQuestions: Int,
    modifier: Modifier = Modifier
) {
    val progress = currentQuestion.toFloat() / maxQuestions.coerceAtLeast(1)
    val percentComplete = (progress * 100).toInt()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    id = R.string.mind_reader_question_progress,
                    currentQuestion,
                    maxQuestions
                ),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText
            )

            Text(
                text = stringResource(id = R.string.mind_reader_progress_complete, percentComplete),
                style = MaterialTheme.typography.labelMedium,
                color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = AppColors.Teal,
            trackColor = AppColors.progressTrackColor,
            strokeCap = StrokeCap.Round
        )
    }
}
