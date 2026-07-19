package com.iti.linguaquest.features.home.presentation.view.components

import com.iti.linguaquest.core.utils.ImageWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors.BrownText
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun WorldCard(
    world: WorldItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLocked = world.unlockLevel != null

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(all = 15.dp)
            .clickable(enabled = !isLocked) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            ImageWrapper(
                model = world.imageSource,
                contentDescription = world.title.asString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp,
                            bottomStart = 20.dp
                        )
                    )
                    .alpha(if (isLocked) 0.5f else 1f),
                contentScale = ContentScale.Crop
            )

            DifficultyChip(
                difficulty = world.difficulty,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )

            if (world.isCompleted && !isLocked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            if (isLocked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.8f))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.unlock_at_level_format, world.unlockLevel),
                        style = AppTextStyles.Caption.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrownText
                        )
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = world.title.asString(),
                color = BrownText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.alpha(if (isLocked) 0.5f else 1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().alpha(if (isLocked) 0.5f else 1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.progress), color = LinguaQuestTheme.colors.iconsColor,
                    style = AppTextStyles.Caption.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "${(world.progress * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            ProgressTrack(
                progress = world.progress,
                trackColor = LinguaQuestTheme.colors.progressTrackRemainedColor,
                fillColor = world.difficulty.badgeColor,
                height = 6.dp,
                modifier = Modifier.alpha(if (isLocked) 0.5f else 1f)
            )
        }
    }
}
