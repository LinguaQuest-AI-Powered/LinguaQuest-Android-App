package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun MindReaderSpeechBubble(
    text: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .shadow(4.dp, shape, spotColor = LinguaQuestTheme.colors.blackColor.copy(alpha = 0.13f))
            .background(LinguaQuestTheme.colors.MindReaderCream, shape)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Text(
            text = text,
            color = LinguaQuestTheme.colors.BrownText,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}
