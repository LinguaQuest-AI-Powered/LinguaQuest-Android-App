package com.iti.linguaquest.features.home.presentation.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    LaunchedEffect(state) {
        when (state) {
            is DailyMissionDialogState.Loading -> {
                soundPlayer.play(AppSound.GettingWord)
            }
            is DailyMissionDialogState.Success -> {
                soundPlayer.stop(AppSound.GettingWord)
                soundPlayer.play(AppSound.FoundWord)
            }
            is DailyMissionDialogState.Hidden -> {
                soundPlayer.stop(AppSound.GettingWord)
            }
        }
    }

    val isSuccess = state is DailyMissionDialogState.Success

    AppDialog(
        title = if (isSuccess) "Daily Mission!" else "Decrypting...",
        message = if (isSuccess) "Your target for today is:" else "Stand by while we fetch today's secret word...",
        onDismissRequest = onDismissRequest,
        showCloseIcon = isSuccess,
        primaryButtonText = if (isSuccess) "Start Camera" else "Loading...",
        isPrimaryButtonEnabled = isSuccess,
        onPrimaryClick = {
            if (state is DailyMissionDialogState.Success) {
                onStartCamera(state.word)
            }
        },
        secondaryButtonText = if (isSuccess) "Later" else null,
        onSecondaryClick = if (isSuccess) onDismissRequest else null,
        customContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state is DailyMissionDialogState.Loading) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gift))
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
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = state.word.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 100.dp) // Pushed down so explosion is above it
                    )
                }
            }
        }
    )
}
