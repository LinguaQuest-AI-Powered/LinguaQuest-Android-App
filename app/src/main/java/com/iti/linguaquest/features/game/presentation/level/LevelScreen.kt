package com.iti.linguaquest.features.game.presentation.level

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.game.presentation.level.components.HintsBottomSheet

import com.iti.linguaquest.features.game.presentation.level.components.QuestCard
import com.iti.linguaquest.features.game.presentation.level.contract.LevelEffect
import com.iti.linguaquest.features.game.presentation.level.contract.LevelIntent
import com.iti.linguaquest.features.game.presentation.level.viewmodel.LevelViewModel

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.utils.ShareTopBar
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
            .background(LinguaQuestTheme.colors.whiteColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShareTopBar(
                titleText = stringResource(id = R.string.level_title, state.levelNumber),
                onBackClick = { viewModel.onIntent(LevelIntent.BackClicked) },
                modifier = Modifier.padding(top = 40.dp),
                trailingContent = {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .background(LinguaQuestTheme.colors.whiteColor, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.MonetizationOn),
                            contentDescription = stringResource(id = R.string.coins),
                            tint = LinguaQuestTheme.colors.OrangeActive,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%,d".format(state.coinCount),
                            color = LinguaQuestTheme.colors.BrownText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
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
