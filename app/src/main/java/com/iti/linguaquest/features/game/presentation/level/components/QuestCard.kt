package com.iti.linguaquest.features.game.presentation.level.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
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
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.IconPosition

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import com.iti.linguaquest.core.sharedComponents.AppGradientBackgroundBox
import androidx.compose.foundation.Image
import com.iti.linguaquest.core.sharedComponents.AppOutlinedButton
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon

@Composable
fun QuestCard(
    wordToGuess: String,
    hintText: String,
    isLoading: Boolean,
    isHintLoading: Boolean,
    isHintConsumed: Boolean,
    onOpenCameraClick: () -> Unit,
    onChangeWordClick: () -> Unit,
    onSoundClick: () -> Unit,
    onMascotClick: () -> Unit,
    isCameraEnabled: Boolean,
    isChangeWordEnabled: Boolean,
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
                            .heightIn(min = 80.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 80.dp)
                                .background(LinguaQuestTheme.colors.ProfileCardColor, RoundedCornerShape(16.dp))
                                .border(1.dp, LinguaQuestTheme.colors.OrangeActive, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                LingoSpinningIcon(
                                    modifier = Modifier.size(40.dp)
                                )
                            } else {
                                AnimatedContent(
                                    targetState = wordToGuess,
                                    label = "wordAnimation"
                                ) { targetWord ->
                                    Text(
                                        text = targetWord,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = LinguaQuestTheme.colors.BrownText,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 36.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(LinguaQuestTheme.colors.ProfileCardColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = onSoundClick) {
                                Icon(
                                    painter = rememberVectorPainter(Icons.Default.VolumeUp),
                                    contentDescription = stringResource(id = R.string.play_sound),
                                    tint = LinguaQuestTheme.colors.BrownText,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isHintLoading) {
                        LingoSpinningIcon(
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = hintText,
                            color = LinguaQuestTheme.colors.iconsColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    AppButton3D(
                        text = stringResource(id = R.string.open_camera),
                        onClick = onOpenCameraClick,
                        variant = ButtonVariant.PRIMARY,
                        iconPosition = IconPosition.START,
                        icon = rememberVectorPainter(Icons.Default.CameraAlt),
                        enabled = isCameraEnabled
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AppOutlinedButton(
                        text = stringResource(id = R.string.change_word),
                        onClick = onChangeWordClick,
                        enabled = isChangeWordEnabled
                    )

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
                    .background(LinguaQuestTheme.colors.whiteColor, RoundedCornerShape(16.dp))
                    .border(1.dp, LinguaQuestTheme.colors.textFieldBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (isHintConsumed) stringResource(id = R.string.change_word_hint) else stringResource(id = R.string.mascot_help_text),
                    color = LinguaQuestTheme.colors.BrownText,
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
