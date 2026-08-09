package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.home.presentation.contract.DailyMissionDialogState

@Composable
fun DailyMissionDialog(
    state: DailyMissionDialogState,
    onDismissRequest: () -> Unit,
    onStartCamera: (String) -> Unit
) {
    if (state is DailyMissionDialogState.Hidden) return

    val soundPlayer = LocalSoundPlayer.current

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.stop(AppSound.GettingWord)
            soundPlayer.stop(AppSound.FoundWord)
        }
    }

    LaunchedEffect(state) {
        when (state) {
            is DailyMissionDialogState.Loading -> soundPlayer.play(
                AppSound.GettingWord,
                loop = true
            )

            is DailyMissionDialogState.Success -> {
                soundPlayer.stop(AppSound.GettingWord)
                soundPlayer.play(AppSound.FoundWord)
            }

            is DailyMissionDialogState.Hidden -> {
                // Handled by onDispose since the dialog leaves the composition
            }
        }
    }

    val isSuccess = state is DailyMissionDialogState.Success

    AppDialog(
        title = if (isSuccess) {
            stringResource(R.string.daily_mission_title_success)
        } else {
            stringResource(R.string.daily_mission_title_loading)
        },
        message = if (isSuccess) {
            stringResource(R.string.daily_mission_message_success)
        } else {
            stringResource(R.string.daily_mission_message_loading)
        },
        onDismissRequest = onDismissRequest,
        showCloseIcon = isSuccess,
        primaryButtonText = if (isSuccess) {
            stringResource(R.string.daily_mission_button_start_camera)
        } else {
            stringResource(R.string.daily_mission_button_loading)
        },
        isPrimaryButtonEnabled = isSuccess,
        onPrimaryClick = {
            if (state is DailyMissionDialogState.Success) {
                onStartCamera(state.word)
            }
        },
        secondaryButtonText = if (isSuccess) stringResource(R.string.daily_mission_button_later) else null,
        onSecondaryClick = if (isSuccess) onDismissRequest else null,
        customContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state is DailyMissionDialogState.Loading) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.waiting_gift))
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.matchParentSize()
                        )
                    } else if (state is DailyMissionDialogState.Success) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.explor_gift))
                        LottieAnimation(
                            composition = composition,
                            iterations = 1,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isSuccess,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    if (state is DailyMissionDialogState.Success) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                .border(
                                    width = 1.5.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(vertical = 14.dp, horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.word.uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Black,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }
            }
        }
    )
}