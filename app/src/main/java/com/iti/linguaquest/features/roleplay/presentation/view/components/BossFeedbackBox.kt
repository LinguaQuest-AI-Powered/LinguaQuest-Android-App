package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun BossFeedbackBox(
    feedbackMessage: String,
    modifier: Modifier = Modifier,
    strengths: List<String> = emptyList(),
    improvements: List<String> = emptyList()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.6f),
                shape = RoundedCornerShape(14.dp)
            )
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (feedbackMessage.isNotBlank()) {
                Text(
                    text = feedbackMessage,
                    style = AppTextStyles.DialogMessage,
                    color = LinguaQuestTheme.colors.BrownText,
                    fontSize = 14.sp
                )
            }

            if (strengths.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.roleplay_strengths),
                    style = AppTextStyles.DialogMessage,
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.SuccessAccent,
                    fontSize = 14.sp
                )
                strengths.forEach { strength ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = LinguaQuestTheme.colors.SuccessAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = strength,
                            style = AppTextStyles.DialogMessage,
                            color = LinguaQuestTheme.colors.BrownText,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            if (improvements.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.roleplay_improvements),
                    style = AppTextStyles.DialogMessage,
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.OrangeActive,
                    fontSize = 14.sp
                )
                improvements.forEach { improvement ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = LinguaQuestTheme.colors.OrangeActive,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = improvement,
                            style = AppTextStyles.DialogMessage,
                            color = LinguaQuestTheme.colors.BrownText,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BossFeedbackBoxPreview() {
    LinguaQuestTheme {
        BossFeedbackBox(
            feedbackMessage = "Great job negotiating!",
            strengths = listOf("Used polite phrasing", "Good pricing vocabulary"),
            improvements = listOf("Try to keep a faster pace")
        )
    }
}
