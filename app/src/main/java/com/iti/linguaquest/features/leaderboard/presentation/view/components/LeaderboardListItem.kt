package com.iti.linguaquest.features.leaderboard.presentation.view.components
import com.iti.linguaquest.core.theme.LinguaQuestTheme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.ImageWrapper
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardEntry

@Composable
fun LeaderboardListItem(entry: LeaderboardEntry, index: Int) {

    val isYou = entry.isCurrentUser
    val borderColor = if (isYou) MaterialTheme.colorScheme.tertiary else LinguaQuestTheme.colors.textFieldBorder
    val textColor = if (isYou) MaterialTheme.colorScheme.tertiary else LinguaQuestTheme.colors.BrownText
    val subtitleColor = if (isYou) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f) else LinguaQuestTheme.colors.titleAndCationsColor.copy(alpha = 0.7f)

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val baseDelay = 1600L
        val staggerStep = 40L
        val maxStaggeredIndex = 15
        val itemDelay = baseDelay + (index.coerceAtMost(maxStaggeredIndex)) * staggerStep
        delay(itemDelay.milliseconds)
        visible = true
    }

    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else 50f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = offsetY.dp)
            .alpha(alphaAnim)
            .padding(horizontal = 16.dp)
            .background(borderColor, RoundedCornerShape(20.dp))
            .padding(bottom = if (isYou) 6.dp else 4.dp)
            .background(LinguaQuestTheme.colors.whiteColor, RoundedCornerShape(20.dp))
            .border(if (isYou) 2.dp else 1.dp, borderColor, RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.rank.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                modifier = Modifier.width(36.dp)
            )

            ImageWrapper(
                model = entry.photoUrl ?: R.drawable.lingo_writing,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(2.dp, borderColor, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    text = "Lv. ${entry.level}",
                    fontSize = 13.sp,
                    color = subtitleColor
                )

            }



            if (isYou) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "YOU",
                        color = LinguaQuestTheme.colors.whiteColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "%,d".format(entry.xp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor
                )
                Text(
                    text = stringResource(R.string.xp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = subtitleColor
                )
            }
        }
    }
}