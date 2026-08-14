package com.iti.linguaquest.features.game.presentation.level

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.sharedComponents.dialog.PriceTagContent
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.game.presentation.level.components.QuestCard
import com.iti.linguaquest.features.game.presentation.level.components.HintsBottomSheet
import com.iti.linguaquest.features.game.presentation.level.contract.LevelEffect
import com.iti.linguaquest.features.game.presentation.level.contract.LevelIntent
import com.iti.linguaquest.features.game.presentation.level.viewmodel.LevelViewModel
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.utils.SpeechManager
import com.iti.linguaquest.core.utils.formatCompact
import com.iti.linguaquest.features.game.presentation.shared.GameSharedViewModel
import com.iti.linguaquest.core.domain.model.GameCost

@Composable
fun LevelScreen(
    worldId: Int,
    levelId: Int,
    levelOrder: Int,
    targetWord: String? = null,
    sharedViewModel: GameSharedViewModel,
    onBack: () -> Unit,
    onStartCamera: () -> Unit,
    viewModel: LevelViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sharedState by sharedViewModel.sharedState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val speechManager = remember { SpeechManager(context) }

    DisposableEffect(speechManager) {
        onDispose {
            speechManager.shutdown()
        }
    }

    val requestChangeWordDialog by sharedViewModel.requestChangeWordDialog.collectAsStateWithLifecycle()

    LaunchedEffect(requestChangeWordDialog) {
        if (requestChangeWordDialog) {
            sharedViewModel.consumeChangeWordDialogRequest()
            viewModel.onIntent(LevelIntent.ChangeWordClicked)
        }
    }

    LaunchedEffect(worldId, levelId) {
        viewModel.loadLevelDetails(worldId, levelId, levelOrder, targetWord)
    }

    LaunchedEffect(state.wordToGuess) {
        if (state.wordToGuess.isNotEmpty()) {
            sharedViewModel.clearHint()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LevelEffect.NavigateBack -> onBack()
                LevelEffect.LaunchCamera -> {
                    sharedViewModel.setWorldAndLevelId(worldId, levelId)
                    sharedViewModel.setTargetWord(state.wordToGuess)
                    sharedViewModel.setTargetLanguage(state.languageCode)
                    onStartCamera()
                }

                is LevelEffect.PlaySound -> {
                    speechManager.speak(effect.word, effect.languageCode)
                }

                is LevelEffect.HintRetrieved -> {
                    sharedViewModel.setHintText(effect.hint)
                }

                LevelEffect.SkipLevel -> {
                }
            }
        }
    }

    if (state.isChangeWordDialogVisible) {
        AppDialog(
            title = stringResource(R.string.change_word_confirm_title),
            message = stringResource(R.string.change_word_confirm_message),
            imageRes = R.drawable.lingo_on_coins,
            onDismissRequest = { viewModel.onIntent(LevelIntent.CancelChangeWordClicked) },
            primaryButtonText = stringResource(R.string.change_word_confirm_action),
            isPrimaryButtonEnabled = state.coinCount >= GameCost.CHANGE_WORD.coins,
            onPrimaryClick = { viewModel.onIntent(LevelIntent.ConfirmChangeWordClicked) },
            secondaryButtonText = stringResource(R.string.change_word_cancel_action),
            onSecondaryClick = { viewModel.onIntent(LevelIntent.CancelChangeWordClicked) },
            customContent = { PriceTagContent(-GameCost.CHANGE_WORD.coins) }
        )
    }

    OfflineAwareContent(isOnline = isOnline, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinguaQuestScreenTopBar(
                    title = stringResource(id = R.string.level_title, state.levelOrder),
                    onBackClicked = { viewModel.onIntent(LevelIntent.BackClicked) },
                    showDivider = true,
                    showCoins = true,
                    coinsCount = state.coinCount
                )

                Spacer(modifier = Modifier.height(16.dp))

                QuestCard(
                    wordToGuess = state.wordToGuess,
                    hintText = sharedState.hintText
                        ?: stringResource(id = R.string.scan_hint_format, state.wordToGuess),
                    isLoading = state.isLoading,
                    isHintLoading = state.isHintLoading,
                    isHintConsumed = sharedState.hintText != null,
                    onOpenCameraClick = { viewModel.onIntent(LevelIntent.OpenCameraClicked) },
                    onChangeWordClick = { viewModel.onIntent(LevelIntent.ChangeWordClicked) },
                    onSoundClick = { viewModel.onIntent(LevelIntent.SoundClicked) },
                    onMascotClick = {
                        if (!state.isLoading && !state.isHintLoading) {
                            if (sharedState.hintText == null) {
                                viewModel.onIntent(LevelIntent.MascotTapped)
                            } else {
                                viewModel.onIntent(LevelIntent.ChangeWordClicked)
                            }
                        }
                    },
                    isCameraEnabled = state.isLevelReady && !state.isLoading,
                    isChangeWordEnabled = state.isLevelReady && !state.isLoading && !state.isHintLoading,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
        if (state.isBottomSheetVisible) {
            HintsBottomSheet(
                coinCount = state.coinCount,
                isLoading = state.isHintLoading,
                onDismiss = { viewModel.onIntent(LevelIntent.DismissBottomSheet) },
                onBuyHint = { viewModel.onIntent(LevelIntent.GetHintClicked) }
            )
        }
    }
}
