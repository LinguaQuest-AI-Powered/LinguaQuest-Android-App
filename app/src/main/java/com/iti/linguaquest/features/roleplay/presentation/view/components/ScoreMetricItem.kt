package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun ScoreMetricItem(
    label: String,
    score: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = LinguaQuestTheme.colors.whiteColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 6.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$score%",
            style = AppTextStyles.DialogMessage,
            fontWeight = FontWeight.ExtraBold,
            color = LinguaQuestTheme.colors.BrownText,
            fontSize = 16.sp
        )
        Text(
            text = label,
            style = AppTextStyles.DialogMessage,
            fontWeight = FontWeight.Medium,
            color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.8f),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreMetricItemPreview() {
    LinguaQuestTheme {
        ScoreMetricItem(
            label = "Fluency",
            score = 85
        )
    }
}
