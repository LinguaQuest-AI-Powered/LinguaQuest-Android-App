package com.iti.linguaquest.features.leaderboard.presentation.view.components
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.leaderboard.domain.model.LeaderboardEntry
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PodiumItem(
    entry: LeaderboardEntry,
    rankColor: Color,
    cardHeight: Dp,
    isFirst: Boolean = false,
    delayMillis: Int,
    animatedIds: MutableSet<Int>
) {
    val avatarSize = if (isFirst) 72.dp else 60.dp

    val alreadyAnimated = remember(entry.userId) { entry.userId in animatedIds }
    var step by remember(entry.userId) { mutableIntStateOf(if (alreadyAnimated) 3 else 0) }

    LaunchedEffect(entry.userId) {
        if (!alreadyAnimated) {
            delay(delayMillis.toLong().milliseconds)
            step = 1
            delay(80.milliseconds)
            step = 2
            delay(100.milliseconds)
            step = 3
            animatedIds.add(entry.userId)
        }
    }

    val lineScaleX by animateFloatAsState(
        targetValue = if (step >= 1) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)
    )

    val animatedCardHeight by animateDpAsState(
        targetValue = if (step >= 2) cardHeight else 0.dp,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium)
    )

    val avatarScale by animateFloatAsState(
        targetValue = if (step >= 3) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(avatarSize)
                .offset(y = 20.dp)
                .zIndex(1f)
                .graphicsLayer {
                    scaleX = avatarScale
                    scaleY = avatarScale
                    alpha = avatarScale.coerceIn(0f, 1f)
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        ambientColor = rankColor,
                        spotColor = rankColor
                    )
                    .background(LinguaQuestTheme.colors.whiteColor, CircleShape)
                    .border(3.dp, rankColor, CircleShape)
            )

            ImageWrapper(
                model = entry.photoUrl ?: R.drawable.lingo_writing,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(avatarSize - 6.dp)
                    .clip(CircleShape)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(if (isFirst) 22.dp else 18.dp)
                    .background(rankColor, CircleShape)
                    .border(2.dp, LinguaQuestTheme.colors.whiteColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_start),
                    contentDescription = null,
                    tint = if (isFirst) LinguaQuestTheme.colors.BrownText else LinguaQuestTheme.colors.whiteColor,
                    modifier = Modifier.size(if (isFirst) 12.dp else 10.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedCardHeight.coerceAtLeast(0.dp))
                .alpha(if (animatedCardHeight > 5.dp) 1f else 0f)
                .then(
                    if (isFirst) Modifier.coloredShadow(
                        color = LinguaQuestTheme.colors.LeaderboardGold,
                        borderRadius = 16.dp,
                        blurRadius = 20.dp,
                        offsetY = 0.dp
                    ) else Modifier
                )
                .border(
                    width = if (isFirst) 1.5.dp else 0.dp,
                    color = if (isFirst) LinguaQuestTheme.colors.LeaderboardGold else Color.Transparent,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isFirst) LinguaQuestTheme.colors.whiteColor else MaterialTheme.colorScheme.secondary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 28.dp, bottom = 8.dp, start = 4.dp, end = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "#${entry.rank}",
                        fontSize = if (isFirst) 20.sp else 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LinguaQuestTheme.colors.BrownText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.username,
                        fontSize = if (isFirst) 15.sp else 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.BrownText,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${entry.xp} XP",
                        fontSize = if (isFirst) 13.sp else 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .graphicsLayer { scaleX = lineScaleX }
                .background(
                    rankColor,
                    RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                )
        )
    }
}