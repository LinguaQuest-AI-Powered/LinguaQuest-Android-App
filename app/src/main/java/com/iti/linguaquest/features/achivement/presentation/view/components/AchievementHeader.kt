package com.iti.linguaquest.features.achivement.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.ImageWrapper
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AchievementHeader(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -50 },
            animationSpec = tween(600)
        ) + fadeIn(animationSpec = tween(600)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            LinguaQuestTheme.colors.AchievementHeaderGradientTop,
                            LinguaQuestTheme.colors.AchievementHeaderGradientBottom
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = LinguaQuestTheme.colors.AchievementCardBorder,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(LinguaQuestTheme.colors.whiteColor, CircleShape)
                        .border(2.dp, LinguaQuestTheme.colors.AchievementCardBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    ImageWrapper(
                        model = R.drawable.lingo_acheviment,
                        contentDescription = stringResource(R.string.achievement_header_trophies_cd),
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "My Trophies",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LinguaQuestTheme.colors.BrownText
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Every word is a step deeper into the\nquest.",
                    fontSize = 13.sp,
                    color = LinguaQuestTheme.colors.titleAndCationsColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
