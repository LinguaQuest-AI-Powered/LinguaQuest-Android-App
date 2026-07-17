package com.iti.linguaquest.features.level.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.level.presentation.components.HintsBottomSheet
import com.iti.linguaquest.features.level.presentation.components.LevelTopBar
import com.iti.linguaquest.features.level.presentation.components.MascotHelp
import com.iti.linguaquest.features.level.presentation.components.QuestCard
import com.iti.linguaquest.features.level.presentation.contract.LevelEffect
import com.iti.linguaquest.features.level.presentation.contract.LevelIntent
import com.iti.linguaquest.features.level.presentation.viewmodel.LevelViewModel

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun LevelScreen(
    worldId: Int,
    levelNumber: Int,
    onBack: () -> Unit,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var playWhenReady by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
            }
        }
        textToSpeech.setSpeechRate(0.8f)
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    LaunchedEffect(isTtsReady, state.languageCode) {
        if (isTtsReady) {
            tts?.language = Locale(state.languageCode)
            if (playWhenReady) {
                tts?.speak(state.wordToGuess, TextToSpeech.QUEUE_FLUSH, null, null)
                playWhenReady = false
            }
        }
    }

    LaunchedEffect(worldId, levelNumber) {
        viewModel.loadLevelDetails(worldId, levelNumber)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LevelEffect.NavigateBack -> onBack()
                LevelEffect.LaunchCamera -> {
                    // TODO: integrate camera feature
                }
                LevelEffect.PlaySound -> {
                    if (isTtsReady) {
                        tts?.speak(state.wordToGuess, TextToSpeech.QUEUE_FLUSH, null, null)
                    } else {
                        playWhenReady = true
                    }
                }
                LevelEffect.SkipLevel -> {
                    // TODO: handle skipping the level
                    onBack()
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF7F2)) // Soft beige background from design
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LevelTopBar(
                levelNumber = state.levelNumber,
                coinCount = state.coinCount,
                onBack = { viewModel.onIntent(LevelIntent.BackClicked) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            MascotHelp(
                onClick = { viewModel.onIntent(LevelIntent.MascotTapped) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            QuestCard(
                wordToGuess = state.wordToGuess,
                hintText = stringResource(id = com.iti.linguaquest.R.string.scan_hint_format, state.wordToGuess),
                onOpenCameraClick = { viewModel.onIntent(LevelIntent.OpenCameraClicked) },
                onSkipClick = { viewModel.onIntent(LevelIntent.SkipClicked) },
                onSoundClick = { viewModel.onIntent(LevelIntent.SoundClicked) },
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        if (state.isBottomSheetVisible) {
            HintsBottomSheet(
                coinCount = state.coinCount,
                onDismiss = { viewModel.onIntent(LevelIntent.DismissBottomSheet) },
                onRevealFirstLetter = { viewModel.onIntent(LevelIntent.RevealFirstLetterClicked) },
                onShowCategoryClue = { viewModel.onIntent(LevelIntent.ShowCategoryClueClicked) }
            )
        }
    }
}

@Preview
@Composable
fun LevelScreenPreview() {
    LevelScreen(
        worldId = 1,
        levelNumber = 1,
        onBack = {}
    )
}