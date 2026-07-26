package com.iti.linguaquest.features.voicegame.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.features.voicegame.presentation.view.components.FlyingCoinBadge
import com.iti.linguaquest.features.voicegame.presentation.view.components.VoiceResultHeader
import com.iti.linguaquest.features.voicegame.presentation.view.components.VoiceResultTopBar
import com.iti.linguaquest.features.voicegame.presentation.viewModel.VoiceResultViewModel
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

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
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val awardsCoins = result.isPassed && result.coinsAwarded > 0
    val walletReady = wallet.coins >= result.coinsAwarded || !awardsCoins

    var displayedCoins by remember {
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

    val confettiColors = listOf(
        LinguaQuestTheme.colors.OrangeActive.toArgb(),
        LinguaQuestTheme.colors.splashTopLeftColor.toArgb(),
        LinguaQuestTheme.colors.whiteColor.toArgb()
    )
    val party = remember {
        Party(
            speed = 0f, maxSpeed = 30f, damping = 0.9f, spread = 360,
            colors = confettiColors,
            position = Position.Relative(0.5, 0.25),
            emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(200)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { containerOrigin = it.positionInRoot() }
    ) {
        if (result.isPassed) {
            KonfettiView(modifier = Modifier.fillMaxSize(), parties = listOf(party))
        }

        Column(modifier = Modifier.fillMaxSize()) {
            VoiceResultTopBar(
                coins = displayedCoins,
                showCoins = walletReady,
                onCoinPillPositioned = { targetOffset = it }
            )

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
