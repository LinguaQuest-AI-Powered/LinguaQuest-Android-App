package com.iti.linguaquest.core.sharedComponents

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme


@Composable
fun LinguaQuestTopAppBar(
    xp: Int,
    lives: Int,
    modifier: Modifier = Modifier
) {
    val animatedXp by animateIntAsState(
        targetValue = xp,
        animationSpec = tween(durationMillis = 600),
        label = "xpAnim"
    )
    val animatedLives by animateIntAsState(
        targetValue = lives,
        animationSpec = tween(durationMillis = 600),
        label = "livesAnim"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.CreamBackground)
            .border(
                width = 0.5.dp,
                color = AppColors.BrownText.copy(alpha = 0.12f),
                shape = RoundedCornerShape(0.dp)
            )
             .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
         Row(verticalAlignment = Alignment.CenterVertically) {

            Image(
                painter = painterResource(id = R.drawable.lingo_app_bar),
                contentDescription = "LinguaQuest Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AppColors.CreamBackground, CircleShape)
                    .border(
                        width = 2.dp,
                        color = AppColors.OrangeActive,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "LinguaQuest",
                color = AppColors.OrangeActive,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.3).sp
            )
        }

         Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatChip(
                iconRes = R.drawable.ic_star,
                value = animatedXp,
                textColor = AppColors.BrownText
            )
            StatChip(
                iconRes = R.drawable.ic_medal,
                value = animatedLives,
                textColor = AppColors.BrownText
            )
        }
    }
}


@Composable
private fun StatChip(
    iconRes: Int,
    value: Int,
    textColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(AppColors.CreamBackground)
            .border(
                width = 1.5.dp,
                color = AppColors.BrownText.copy(alpha = 0.18f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value.toString(),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
private fun LinguaQuestTopAppBarPreview() {
    LinguaQuestTheme {
        LinguaQuestTopAppBar(xp = 1250, lives = 45)
    }
}
