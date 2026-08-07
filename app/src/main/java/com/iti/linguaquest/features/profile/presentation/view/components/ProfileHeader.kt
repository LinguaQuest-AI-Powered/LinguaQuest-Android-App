package com.iti.linguaquest.features.profile.presentation.view.components


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper
import com.iti.linguaquest.features.profile.presentation.model.ProfileState


@Composable
fun ProfileHeader(state: ProfileState, onEditAvatarClick: () -> Unit, isAvatarUploading: Boolean) {
    var showFullScreenAvatar by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val avatarScale by animateFloatAsState(
                targetValue = if (isPressed) 0.93f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "avatarScale"
            )
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .graphicsLayer { scaleX = avatarScale; scaleY = avatarScale }
                    .clip(CircleShape)
                    .border(3.dp, LinguaQuestTheme.colors.OrangeActive, CircleShape)
                    .padding(4.dp)
            ) {
                ImageWrapper(
                    model = state.avatarUrl,
                    contentDescription = state.userName,
                    placeholder = painterResource(R.drawable.lingo),
                    error = painterResource(R.drawable.lingo),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            showFullScreenAvatar = true
                        },
                    contentScale = ContentScale.Crop
                )

                if (isAvatarUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f)),
                        contentAlignment = Alignment.Center
                    ) {
                        LingoSpinningIcon(
                            size = 28.dp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = (-6).dp, y = (-6).dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.iconsColor)
                    .border(2.dp, LinguaQuestTheme.colors.BrownText, CircleShape)
                    .clickable { onEditAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_edit_pen),
                    contentDescription = "edit",
                    modifier = Modifier.size(14.dp),
                    colorFilter = ColorFilter.tint(LinguaQuestTheme.colors.whiteColor)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            state.userName,
            color = LinguaQuestTheme.colors.blackColor,
            style = AppTextStyles.Label.copy(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .background(
                    color = LinguaQuestTheme.colors.BrownText,
                    shape = RoundedCornerShape(50)
                )
                .padding(bottom = 3.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_profile_level),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "${stringResource(R.string.level)} ${state.level}",
                color = LinguaQuestTheme.colors.BrownText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showFullScreenAvatar) {
        FullScreenZoomableAvatar(
            model = state.avatarUrl,
            contentDescription = state.userName,
            onDismiss = { showFullScreenAvatar = false }
        )
    }
}


