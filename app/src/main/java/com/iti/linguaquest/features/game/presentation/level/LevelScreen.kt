package com.iti.linguaquest.features.game.presentation.level

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.game.presentation.level.components.HintsBottomSheet
import com.iti.linguaquest.features.game.presentation.level.components.LevelTopBar
import com.iti.linguaquest.features.game.presentation.level.components.QuestCard
import com.iti.linguaquest.features.game.presentation.level.contract.LevelEffect
import com.iti.linguaquest.features.game.presentation.level.contract.LevelIntent
import com.iti.linguaquest.features.game.presentation.level.viewmodel.LevelViewModel

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.SpeechManager
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel

@Composable
fun LevelScreen(
    worldId: Int,
    levelNumber: Int,
    sharedViewModel: GameSharedViewModel,
    onBack: () -> Unit,
    onStartCamera: () -> Unit,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val speechManager = remember { SpeechManager(context) }

    DisposableEffect(speechManager) {
        onDispose {
            speechManager.shutdown()
        }
    }

    LaunchedEffect(worldId, levelNumber) {
        viewModel.loadLevelDetails(worldId, levelNumber)
    }

    LaunchedEffect(worldId, levelNumber) {
        viewModel.loadLevelDetails(worldId, levelNumber)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LevelEffect.NavigateBack -> onBack()
                LevelEffect.LaunchCamera -> {
                    sharedViewModel.setTargetWord(state.wordToGuess)
                    onStartCamera()
                }
                is LevelEffect.PlaySound -> {
                    speechManager.speak(effect.word, effect.languageCode)
                }
                LevelEffect.SkipLevel -> {
                    onBack()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF7F2)),
        contentAlignment = Alignment.Center
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

            Spacer(modifier = Modifier.height(16.dp))

            QuestCard(
                wordToGuess = state.wordToGuess,
                hintText = stringResource(id = R.string.scan_hint_format, state.wordToGuess),
                onOpenCameraClick = { viewModel.onIntent(LevelIntent.OpenCameraClicked) },
                onSkipClick = { viewModel.onIntent(LevelIntent.SkipClicked) },
                onSoundClick = { viewModel.onIntent(LevelIntent.SoundClicked) },
                onMascotClick = { viewModel.onIntent(LevelIntent.MascotTapped) },
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
