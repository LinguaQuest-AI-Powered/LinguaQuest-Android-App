package com.iti.linguaquest.features.help.presentation.guide.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun UserGuideSectionCard(
    emoji: String,
    title: String,
    description: String,
    accentColor: Color,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    descriptionColor: Color = LinguaQuestTheme.colors.iconsColor,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false
) {
    val cardBackground = if (highlighted) {
        accentColor.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBackground)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = titleColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth(0.28f)
                .clip(RoundedCornerShape(50))
                .background(accentColor)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            color = descriptionColor,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}
