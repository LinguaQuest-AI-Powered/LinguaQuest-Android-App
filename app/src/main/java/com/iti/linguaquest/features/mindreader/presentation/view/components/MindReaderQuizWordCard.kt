package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun MindReaderQuizWordCard(
    word: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = word,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = LinguaQuestTheme.colors.OrangeActive,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, LinguaQuestTheme.colors.OrangeActive, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    )
}
