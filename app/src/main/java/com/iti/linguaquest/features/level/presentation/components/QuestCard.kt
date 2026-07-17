package com.iti.linguaquest.features.level.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition
import com.iti.linguaquest.core.theme.AppColors

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import com.iti.linguaquest.core.sharedComponents.AppGradientBackgroundBox
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import com.iti.linguaquest.core.theme.AppTextStyles

@Composable
fun QuestCard(
    wordToGuess: String,
    hintText: String,
    onOpenCameraClick: () -> Unit,
    onSkipClick: () -> Unit,
    onSoundClick: () -> Unit,
    onMascotClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageSize = 120.dp
    val imageHalfSize = imageSize / 2

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            AppGradientBackgroundBox(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .padding(top = imageHalfSize - 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .background(Color(0xFFFFF7E6), RoundedCornerShape(16.dp))
                                .border(1.dp, Color(0xFFFFB347), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = wordToGuess,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AppColors.BrownText
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color(0xFFFFF7E6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = onSoundClick) {
                                Icon(
                                    painter = rememberVectorPainter(Icons.Default.VolumeUp),
                                    contentDescription = stringResource(id = R.string.play_sound),
                                    tint = AppColors.BrownText,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = hintText,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    AppButton(
                        text = stringResource(id = R.string.open_camera),
                        onClick = onOpenCameraClick,
                        variant = ButtonVariant.PRIMARY,
                        iconPosition = IconPosition.START,
                        icon = rememberVectorPainter(Icons.Default.CameraAlt)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onSkipClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(2.dp, AppColors.DialogSecondaryButtonOutline),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = AppColors.DialogSecondaryButtonOutline
                        ),
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.skip),
                            style = AppTextStyles.Button,
                            color = AppColors.DialogSecondaryButtonOutline
                        )
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onMascotClick
                )
        ) {
            Box(
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.mascot_help_text),
                    color = AppColors.BrownText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.lingo),
                contentDescription = stringResource(id = R.string.mascot_content_desc),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(imageSize)
            )
        }
    }
}
