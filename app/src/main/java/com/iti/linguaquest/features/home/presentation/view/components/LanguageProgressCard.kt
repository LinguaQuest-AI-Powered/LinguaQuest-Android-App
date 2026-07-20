package com.iti.linguaquest.features.home.presentation.view.components

import com.iti.linguaquest.core.utils.ImageWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iti.linguaquest.core.sharedComponents.text.UiText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun LanguageProgressCard(
    languageName: UiText,
    level: Int,
    streakDays: Int,
    progress: Float,
    flagSource: Any?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                ImageWrapper(
                    model = flagSource,
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.currently_language),
                    color = LinguaQuestTheme.colors.iconsColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = languageName.asString(),
                    color = LinguaQuestTheme.colors.blackColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                LevelLabel(level = level)
                Spacer(modifier = Modifier.height(4.dp))
                StreakBadge(streakDays = streakDays)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        ProgressTrack(
            progress = progress,
            trackColor = LinguaQuestTheme.colors.progressTrackRemainedColor,
            fillColor = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
private fun LevelLabel(level: Int) {
    Text(
        text = "${stringResource(R.string.level)} $level",
        color = LinguaQuestTheme.colors.iconsColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun StreakBadge(streakDays: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.streak_icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$streakDays ${stringResource(R.string.days)}",
            color = LinguaQuestTheme.colors.iconsColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
internal fun ProgressTrack(
    progress: Float,
    trackColor: Color,
    fillColor: Color,
    modifier: Modifier = Modifier,
    height: Dp = 15.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .height(height)
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            fillColor.copy(alpha = 0.4f),
                            fillColor
                        )
                    )
                )
        )
    }
}