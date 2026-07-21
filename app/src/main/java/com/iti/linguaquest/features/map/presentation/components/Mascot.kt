package com.iti.linguaquest.features.map.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R

@Composable
fun Mascot(offsetX: Dp, offsetY: Dp) {
    Box(
        modifier = Modifier
            .absoluteOffset(x = offsetX, y = offsetY)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(LinguaQuestTheme.colors.whiteColor, RoundedCornerShape(16.dp))
                    .border(2.dp, LinguaQuestTheme.colors.BrownText, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Let's Learn!",
                    color = LinguaQuestTheme.colors.BrownText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = painterResource(id = R.drawable.lingo_onboarding_1),
                contentDescription = "Mascot",
                modifier = Modifier.size(100.dp)
            )
        }
    }
}
