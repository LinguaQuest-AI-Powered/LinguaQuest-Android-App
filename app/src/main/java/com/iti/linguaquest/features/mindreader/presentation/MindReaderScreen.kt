package com.iti.linguaquest.features.mindreader.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderPhase
import com.iti.linguaquest.features.mindreader.presentation.components.PreGameLobbyContent
import com.iti.linguaquest.features.mindreader.presentation.components.ActiveGameContent
import com.iti.linguaquest.features.mindreader.presentation.components.GuessRevealContent
import com.iti.linguaquest.features.mindreader.presentation.components.PopQuizContent
import com.iti.linguaquest.features.mindreader.presentation.components.AkinatorTrapContent
import com.iti.linguaquest.features.mindreader.presentation.components.ResultContent
import com.iti.linguaquest.features.mindreader.presentation.components.LoadingGuessContent

@Composable
fun MindReaderScreen(
    viewModel: MindReaderViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Assuming we handle effects elsewhere or inline
    // (We will use a LaunchedEffect block if needed for navigation back, toasts, etc.)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Crossfade(targetState = state.currentPhase, label = "PhaseCrossfade") { phase ->
            when (phase) {
                MindReaderPhase.LOBBY -> PreGameLobbyContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
                MindReaderPhase.PLAYING -> ActiveGameContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
                MindReaderPhase.GUESSING_LOADING -> LoadingGuessContent()
                MindReaderPhase.GUESS_REVEAL -> GuessRevealContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
                MindReaderPhase.POP_QUIZ -> PopQuizContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
                MindReaderPhase.STUMP -> AkinatorTrapContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
                MindReaderPhase.RESULT -> ResultContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
            }
        }
    }
}
