package com.iti.linguaquest.features.home.presentation.view.components.daily_rewards_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer

@Composable
fun HomeDailyRewardBannerWrapper(
    isVisible: Boolean,
    showCoinRain: Boolean,
    fallZoneHeight: Dp,
    bannerHeightPx: Float,
    onBannerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val soundPlayer = LocalSoundPlayer.current

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ) + fadeIn(tween(400)),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        ) + fadeOut(tween(300)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fallZoneHeight)
        ) {
            AnimatedVisibility(
                visible = showCoinRain,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(800)),
                modifier = Modifier.matchParentSize()
            ) {
                CoinRainOverlay(
                    modifier = Modifier.fillMaxSize(),
                    coinCount = 30,
                    startYPx = bannerHeightPx
                )
            }

            DailyStreakBonusBanner(
                onClick = {
                    soundPlayer.play(AppSound.DAILY_REWARD)
                    onBannerClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}
