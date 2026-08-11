package com.iti.linguaquest.features.voicegame.presentation.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.sound.AppSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.features.voicegame.presentation.viewModel.VoiceResultViewModel
import com.iti.linguaquest.core.sharedComponents.AppConfettiView
import com.iti.linguaquest.core.wallet.domain.model.Wallet
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun VoiceResultScreen(
    result: VoiceResultUi,
    onContinue: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VoiceResultViewModel = hiltViewModel()
) {
    val soundPlayer = LocalSoundPlayer.current
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()

    VoiceResultContent(
        result = result,
        isOnline = isOnline,
        wallet = wallet,
        soundPlayer = soundPlayer,
        onContinue = onContinue,
        onRetry = onRetry,
        onHome = onHome,
        modifier = modifier
    )
}

@Composable
fun VoiceResultContent(
    result: VoiceResultUi,
    isOnline: Boolean,
    wallet: Wallet,
    soundPlayer: AppSoundPlayer,
    onContinue: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val awardsCoins = result.isPassed && result.coinsAwarded > 0
    val walletReady = wallet.coins >= result.coinsAwarded || !awardsCoins

    var displayedCoins by remember(result.coinsBeforeAward) {
        mutableIntStateOf(result.coinsBeforeAward)
    }

    var containerOrigin by remember { mutableStateOf<Offset?>(null) }
    var sourceOffset by remember { mutableStateOf<Offset?>(null) }
    var targetOffset by remember { mutableStateOf<Offset?>(null) }
    var hasTriggeredFlight by remember { mutableStateOf(false) }
    var showFlyingCoin by remember { mutableStateOf(false) }

    LaunchedEffect(result.isPassed) {
        soundPlayer.play(if (result.isPassed) AppSound.SUCCESS else AppSound.FAIL)
    }

    LaunchedEffect(sourceOffset, targetOffset, walletReady) {
        if (awardsCoins && !hasTriggeredFlight && walletReady && sourceOffset != null && targetOffset != null) {
            hasTriggeredFlight = true
            showFlyingCoin = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { containerOrigin = it.positionInRoot() }
    ) {
        if (result.isPassed) {
            AppConfettiView()
        }

        Column(modifier = Modifier.fillMaxSize()) {
            VoiceResultTopBar(
                coins = displayedCoins,
                showCoins = walletReady,
                onCoinPillPositioned = { targetOffset = it }
            )
            OfflineAwareContent(
                isOnline = isOnline,
                modifier = modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (result.isPassed) {
                        AppConfettiView()
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(12.dp))
                        VoiceResultHeader(
                            isPassed = result.isPassed,
                            advice = result.advice,
                            rating = result.rating,
                            correctWords = result.correctWords,
                            wrongWords = result.wrongWords,
                            coinsAwarded = result.coinsAwarded,
                            xpAwarded = result.xpAwarded,
                            sentence = result.sentence,
                            hideCoinsBadge = hasTriggeredFlight,
                            onCoinsBadgePositioned = { sourceOffset = it },
                            onContinue = onContinue,
                            onRetry = onRetry,
                            onHome = onHome
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                }

                if (showFlyingCoin && sourceOffset != null && targetOffset != null && containerOrigin != null) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            FlyingCoinBadge(
                                coinsAwarded = result.coinsAwarded,
                                sourceOffset = sourceOffset!!,
                                targetOffset = targetOffset!!,
                                containerOrigin = containerOrigin!!,
                                onLanded = {
                                    soundPlayer.play(AppSound.AddedMoney)
                                    displayedCoins = wallet.coins
                                    showFlyingCoin = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VoiceResultPreview() {
    LinguaQuestTheme {
        VoiceResultContent(
            result = VoiceResultUi(
                rating = 8,
                correctWords = listOf("apple", "banana"),
                wrongWords = listOf("cherry"),
                advice = "Great pronunciation!",
                coinsAwarded = 5,
                xpAwarded = 10,
                isPassed = true,
                lessonId = 1,
                sentence = "An apple a day",
                coinsBeforeAward = 100,
                xpBeforeAward = 500
            ),
            onContinue = {},
            onRetry = {},
            onHome = {},
            isOnline = true,
            wallet = Wallet(xp = 500, coins = 100),
            soundPlayer = LocalSoundPlayer.current
        )
    }
}