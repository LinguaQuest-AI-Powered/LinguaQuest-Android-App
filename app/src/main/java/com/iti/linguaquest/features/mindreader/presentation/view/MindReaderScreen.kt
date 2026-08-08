package com.iti.linguaquest.features.mindreader.presentation.view

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.utils.SpeechManager
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderEffect
import com.iti.linguaquest.features.mindreader.presentation.contract.MindReaderPhase
import com.iti.linguaquest.features.mindreader.presentation.view.contents.ActiveGameContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.AkinatorLobbyContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.AkinatorTrapContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.GuessRevealContent
import com.iti.linguaquest.core.sharedComponents.LoadingView
import com.iti.linguaquest.features.mindreader.presentation.view.contents.PopQuizContent
import com.iti.linguaquest.features.mindreader.presentation.view.contents.ResultContent
import com.iti.linguaquest.features.mindreader.presentation.viewmodel.MindReaderViewModel

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

    OfflineAwareContent(
        isOnline = isOnline,
        topBarTitle = stringResource(id = R.string.mind_reader_card_title),
        onBackClicked = onNavigateBack,
        showCoins = true,
        coinsCount = state.coinBalance,
        modifier = Modifier.fillMaxSize()
    ) {
        Crossfade(
            targetState = state.currentPhase,
            label = "PhaseCrossfade",
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { phase ->
            when (phase) {
                MindReaderPhase.LOBBY -> AkinatorLobbyContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
                MindReaderPhase.PLAYING -> ActiveGameContent(
                    state = state,
                    onIntent = viewModel::onIntent
                )
                MindReaderPhase.THINKING,
                MindReaderPhase.GUESSING_LOADING -> LoadingView(
                    message = stringResource(id = R.string.mind_reader_thinking),
                    imageRes = R.drawable.lingo_mind_processing
                )
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
