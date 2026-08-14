package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.mindreader.domain.model.MindReaderCategory

@Composable
fun CategorySelectionCard(
    category: MindReaderCategory?,
    modifier: Modifier = Modifier,
    languageCode: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(LinguaQuestTheme.colors.MindReaderBeige, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(
            text = category?.emoji ?: "📋",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = stringResource(id = R.string.mind_reader_current_category),
                style = MaterialTheme.typography.labelSmall,
                color = LinguaQuestTheme.colors.BrownText.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = category?.resolveDisplayName(languageCode) ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.BrownText
            )
        }
    }
}
