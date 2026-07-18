package com.iti.linguaquest.features.game.presentation.processing.view.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import kotlinx.coroutines.delay

@Composable
fun ProcessingStatusText(modifier: Modifier = Modifier) {
    val phrases = listOf(
        stringResource(id = R.string.game_processing_status_inspecting),
        stringResource(id = R.string.game_processing_status_consulting),
        stringResource(id = R.string.game_processing_status_squinting),
        stringResource(id = R.string.game_processing_status_evaluating),
        stringResource(id = R.string.game_processing_status_almost_there)
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            currentIndex = (currentIndex + 1) % phrases.size
        }
    }

    AnimatedContent(
        targetState = currentIndex,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "PhraseCrossfade",
        modifier = modifier
    ) { targetIndex ->
        Text(
            text = phrases[targetIndex],
            color = LinguaQuestTheme.colors.whiteColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}