package com.iti.linguaquest.features.profile.presentation.editprofile.view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.utils.ImageWrapper

@Composable
fun EditableAvatar(
    avatarModel: Any?,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 110.dp,
    isAvatarUploading: Boolean = false,
    avatarContentDescription: String? = null,
    editButtonContentDescription: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(BorderStroke(3.dp, AppColors.OrangeActive), CircleShape)
            ) {
                ImageWrapper(
                    model = avatarModel ?: R.drawable.lingo_app_bar,
                    contentDescription = avatarContentDescription,
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                if (isAvatarUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = LinguaQuestTheme.colors.whiteColor,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }

                Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.whiteColor)
                    .padding(2.5.dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.blackColor)
                    .clickable(
                        onClick = onEditClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, radius = 15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = editButtonContentDescription,
                    tint = LinguaQuestTheme.colors.whiteColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}