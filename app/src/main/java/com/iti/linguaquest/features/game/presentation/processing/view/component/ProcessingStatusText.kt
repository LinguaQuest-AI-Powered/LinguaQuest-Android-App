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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

@Composable
fun ProcessingStatusText(modifier: Modifier = Modifier) {
    val phrases = listOf(
        "Lingo is inspecting the pixels...",
        "Consulting the dictionary...",
        "Squinting really hard...",
        "Evaluating your photography skills...",
        "Almost there..."
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500) // Change phrase every 2.5 seconds
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
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}