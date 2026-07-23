package com.iti.linguaquest.features.voicegame.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.features.voicegame.presentation.view.components.VoiceResultActionButtons
import com.iti.linguaquest.features.voicegame.presentation.view.components.VoiceResultHeader
import com.iti.linguaquest.features.voicegame.presentation.view.components.VoiceResultScoreSection
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
    modifier: Modifier = Modifier
) {
    val soundPlayer = LocalSoundPlayer.current

    LaunchedEffect(result.isPassed) {
        if (result.isPassed) {
            soundPlayer.play(AppSound.SUCCESS)
        } else {
            soundPlayer.play(AppSound.FAIL)
        }
    }

    val confettiColors = listOf(
        LinguaQuestTheme.colors.OrangeActive.toArgb(),
        LinguaQuestTheme.colors.splashTopLeftColor.toArgb(),
        LinguaQuestTheme.colors.whiteColor.toArgb()
    )

    val party = remember {
        Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = confettiColors,
            position = Position.Relative(0.5, 0.25),
            emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(200)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (result.isPassed) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(party)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            VoiceResultHeader(
                isPassed = result.isPassed,
                advice = result.advice
            )

            Spacer(Modifier.height(16.dp))

            VoiceResultScoreSection(
                rating = result.rating,
                isPassed = result.isPassed,
                correctWords = result.correctWords,
                wrongWords = result.wrongWords,
                coinsAwarded = result.coinsAwarded
            )

            Spacer(Modifier.height(24.dp))

            VoiceResultActionButtons(
                isPassed = result.isPassed,
                onContinue = onContinue,
                onRetry = onRetry,
                onHome = onHome
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
