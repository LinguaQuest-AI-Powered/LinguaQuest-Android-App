package com.iti.linguaquest.features.voicegame.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.core.utils.formatCompact
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.navigation.SharedVoiceResultHolder
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameEffect
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameIntent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGamePhase
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.features.voicegame.presentation.view.components.RecordingConfirmationDialog
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.features.voicegame.presentation.view.contents.EvaluatingPhaseContent
import com.iti.linguaquest.features.voicegame.presentation.view.contents.IdlePhaseContent
import com.iti.linguaquest.features.voicegame.presentation.view.contents.RecordingPhaseContent
import com.iti.linguaquest.features.voicegame.presentation.viewModel.VoiceGameViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun VoiceGameScreen(
    onNavigateBack: () -> Unit,
    onEvaluationComplete: (VoiceResultUi) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VoiceGameViewModel = hiltViewModel()
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler {
        onNavigateBack()
    }

    LaunchedEffect(isOnline) {
        if (isOnline) {
            if (SharedVoiceResultHolder.autoGenerateNextSentence) {
                SharedVoiceResultHolder.autoGenerateNextSentence = false
                viewModel.onIntent(VoiceGameIntent.GenerateNewSentenceClicked)
            } else {
                viewModel.onIntent(VoiceGameIntent.Init)
            }
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onIntent(if (granted) VoiceGameIntent.MicPermissionGranted else VoiceGameIntent.MicPermissionDenied)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                VoiceGameEffect.RequestMicPermission -> {
                    val granted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                    if (granted) viewModel.onIntent(VoiceGameIntent.MicPermissionGranted)
                    else micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }

                is VoiceGameEffect.NavigateToResult -> onEvaluationComplete(effect.result)
            }
        }
    }

    OfflineAwareContent(
        isOnline = isOnline,
        modifier = modifier.fillMaxSize()
    ) {
    Column(modifier = modifier.fillMaxSize()) {
        LinguaQuestScreenTopBar(
            title = stringResource(id = R.string.voice_game_title),
            onBackClicked = onNavigateBack,
            showDivider = true,
            showCoins = true,
            coinsCount = wallet.coins
        )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                when (state.phase) {
                    VoiceGamePhase.IDLE -> IdlePhaseContent(state, viewModel)
                    VoiceGamePhase.RECORDING -> RecordingPhaseContent(state, viewModel)
                    VoiceGamePhase.EVALUATING -> EvaluatingPhaseContent()
                }
            }
        }

        if (state.showConfirmationDialog) {
            RecordingConfirmationDialog(
                playbackSeconds = state.previewPlaybackSeconds,
                isPlaying = state.isPreviewPlaying,
                onTogglePlayback = { viewModel.onIntent(VoiceGameIntent.TogglePreviewPlaybackClicked) },
                onDiscard = { viewModel.onIntent(VoiceGameIntent.DiscardClicked) },
                onProcess = { viewModel.onIntent(VoiceGameIntent.ConfirmProcessClicked) }
            )
        }
    }
}
