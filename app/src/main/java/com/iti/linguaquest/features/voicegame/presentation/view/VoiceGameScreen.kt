package com.iti.linguaquest.features.voicegame.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameEffect
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameIntent
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGamePhase
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.features.voicegame.presentation.view.components.RecordingConfirmationDialog
import com.iti.linguaquest.features.voicegame.presentation.view.components.VoiceGameTopBar
import com.iti.linguaquest.features.voicegame.presentation.view.contents.EvaluatingPhaseContent
import com.iti.linguaquest.features.voicegame.presentation.view.contents.IdlePhaseContent
import com.iti.linguaquest.features.voicegame.presentation.view.contents.RecordingPhaseContent
import com.iti.linguaquest.features.voicegame.presentation.viewModel.VoiceGameViewModel
import com.iti.linguaquest.core.navigation.SharedVoiceResultHolder
import kotlinx.coroutines.flow.collectLatest

@Composable
fun VoiceGameScreen(
    sentence: String,
    lessonId: Int,
    onNavigateBack: () -> Unit,
    onEvaluationComplete: (VoiceResultUi) -> Unit,
    modifier: Modifier = Modifier,
    coins: Int = 1200,
    viewModel: VoiceGameViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (SharedVoiceResultHolder.autoGenerateNextSentence) {
            SharedVoiceResultHolder.autoGenerateNextSentence = false
            viewModel.onIntent(VoiceGameIntent.GenerateNewSentenceClicked)
        } else {
            viewModel.onIntent(VoiceGameIntent.Init(sentence, lessonId))
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

    Column(modifier = modifier.fillMaxSize().padding(vertical = 25.dp)) {
        VoiceGameTopBar(
            coins = coins,
            onNavigateBack = onNavigateBack
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