package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.features.home.presentation.contract.HomeState

@Composable
fun HomeDailyRewardDialog(
    state: HomeState,
    onDismissRequest: () -> Unit,
    onClaimClick: () -> Unit
) {
    val soundPlayer = LocalSoundPlayer.current

    if (state.isDailyRewardDialogVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                DailyRewardCard(
                    currentDay = state.dailyReward?.currentDay ?: 1,
                    cycleLength = state.dailyReward?.cycleLength ?: 5,
                    rewardAmount = state.dailyReward?.rewardCoins ?: 0,
                    rewardXp = state.dailyReward?.rewardXp,
                    isClaiming = state.isClaimingReward,
                    onClaimClick = {
                        soundPlayer.play(AppSound.COIN)
                        onClaimClick()
                    }
                )
            }
        }
    }
}
