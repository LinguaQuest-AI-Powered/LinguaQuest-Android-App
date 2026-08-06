package com.iti.linguaquest.features.mindreader.presentation.view

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.activity.compose.BackHandler
import com.iti.linguaquest.core.utils.SpeechManager
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderEffect
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderPhase
import com.iti.linguaquest.features.mindreader.presentation.viewmodel.MindReaderViewModel
import com.iti.linguaquest.features.mindreader.presentation.view.contents.AkinatorLobbyContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.ActiveGameContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.GuessRevealContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.PopQuizContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.AkinatorTrapContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.ResultContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.LoadingGuessContent

@Composable
fun MindReaderScreen(
    viewModel: MindReaderViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val speechManager = remember { SpeechManager(context) }

    BackHandler {
        onNavigateBack()
    }

    DisposableEffect(Unit) {
        onDispose {
            speechManager.shutdown()
        }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MindReaderEffect.NavigateBack -> onNavigateBack()
                is MindReaderEffect.PlayAudio -> {
                    speechManager.speak(effect.text, effect.languageCode)
                }
                is MindReaderEffect.ShowToast -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Crossfade(targetState = state.currentPhase, label = "PhaseCrossfade") { phase ->
            when (phase) {
                MindReaderPhase.LOBBY -> AkinatorLobbyContent(
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
