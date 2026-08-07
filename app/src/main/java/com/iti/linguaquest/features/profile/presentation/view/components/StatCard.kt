package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.sharedComponents.Card3DWrapper
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun StatCard(
    painter: Painter,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    ledgeHeight: Dp = 4.dp,
    cornerRadius: Dp = 16.dp
) {
    Card3DWrapper(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = LinguaQuestTheme.colors.ProfileCardColor,
        borderColor = LinguaQuestTheme.colors.Sand,
        onClick = onClick,
        ledgeHeight = ledgeHeight,
        cornerRadius = cornerRadius
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                text = value,
                color = LinguaQuestTheme.colors.blackColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label.uppercase(),
                color = LinguaQuestTheme.colors.iconsColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}