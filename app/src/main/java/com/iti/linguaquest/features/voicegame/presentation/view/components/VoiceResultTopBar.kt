package com.iti.linguaquest.features.voicegame.presentation.view.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun VoiceResultTopBar(
    coins: Int,
    showCoins: Boolean,
    onCoinPillPositioned: (Offset) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(LinguaQuestTheme.colors.whiteColor)
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .onGloballyPositioned { cords ->
                    val center = cords.positionInRoot() + Offset(
                        cords.size.width / 2f,
                        cords.size.height / 2f
                    )
                    onCoinPillPositioned(center)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            if (showCoins) {
                Text(
                    "$coins",
                    fontWeight = FontWeight.Bold,
                    color = LinguaQuestTheme.colors.iconsColor
                )
            }
        }
    }
}