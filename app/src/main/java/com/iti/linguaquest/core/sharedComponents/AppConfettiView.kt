package com.iti.linguaquest.core.sharedComponents

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun AppConfettiView(
    modifier: Modifier = Modifier,
    durationMillis: Long = 200,
    maxParticles: Int = 200
) {
    if (LocalInspectionMode.current) return

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
            emitter = Emitter(duration = durationMillis, TimeUnit.MILLISECONDS).max(maxParticles)
        )
    }

    KonfettiView(
        modifier = modifier.fillMaxSize(),
        parties = listOf(party)
    )
}
