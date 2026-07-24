package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun StatCard(
    painter: Painter,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .background(
                color = LinguaQuestTheme.colors.Sand,
                shape = cardShape
            )
            .padding(bottom = 5.dp)
            .clip(cardShape)
            .background(
                color = LinguaQuestTheme.colors.ProfileCardColor,
                shape = cardShape
            )
            .border(
                width = 1.dp,
                color = LinguaQuestTheme.colors.Sand,
                shape = cardShape
            )
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            value,
            color = LinguaQuestTheme.colors.blackColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            label.uppercase(),
            color = LinguaQuestTheme.colors.iconsColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}