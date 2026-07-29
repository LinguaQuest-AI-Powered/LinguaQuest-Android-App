package com.iti.linguaquest.features.help.presentation.contact.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun ContactInfoCard(
    title: String,
    content: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, accentColor.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = accentColor,
            fontSize = 12.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Text(
            text = content,
            modifier = Modifier.padding(top = 6.dp),
            color = LinguaQuestTheme.colors.BrownText,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}
