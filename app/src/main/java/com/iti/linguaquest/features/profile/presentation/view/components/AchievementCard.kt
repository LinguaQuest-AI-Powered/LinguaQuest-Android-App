package com.iti.linguaquest.features.profile.presentation.view.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.profile.presentation.model.Achievement

@Composable
fun AchievementCard(
    achievement: Achievement,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    ledgeHeight: Dp = 4.dp,
    cornerRadius: Dp = 16.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressOffset by animateDpAsState(
        targetValue = if (isPressed && onClick != null) ledgeHeight else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "achievementCardPressOffset"
    )

    val shape = RoundedCornerShape(cornerRadius)
    val cardColor = LinguaQuestTheme.colors.ProfileCardColor
    val borderColor = LinguaQuestTheme.colors.ProfileCardBorderColor

    Box(
        modifier = modifier.padding(bottom = ledgeHeight)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = ledgeHeight)
                .clip(shape)
                .background(borderColor)
        )

        Surface(
            shape = shape,
            color = cardColor,
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = pressOffset)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LinguaQuestTheme.colors.IconBoxBackground),
                    contentAlignment = Alignment.Center
                ) {
                    ImageWrapper(
                        model = R.drawable.ic_cup,
                        contentDescription = achievement.title,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = achievement.title,
                        color = LinguaQuestTheme.colors.blackColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1
                    )

                    Text(
                        text = achievement.progressLabel,
                        color = LinguaQuestTheme.colors.iconsColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}