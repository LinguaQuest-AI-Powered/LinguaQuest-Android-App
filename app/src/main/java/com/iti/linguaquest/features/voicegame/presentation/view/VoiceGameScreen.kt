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
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
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
import com.iti.linguaquest.features.voicegame.presentation.contract.VoiceGameState
import com.iti.linguaquest.features.voicegame.presentation.model.VoiceResultUi
import com.iti.linguaquest.features.voicegame.presentation.view.components.RecordingConfirmationDialog
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.linguaquest.features.voicegame.presentation.view.components.EvaluatingPhaseContent
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import com.iti.linguaquest.core.sharedComponents.PushToTalkButton
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.MessageBubble
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.AppTextStyles
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.voicegame.presentation.view.components.formatElapsed
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
        topBarTitle = stringResource(id = R.string.voice_game_title),
        onBackClicked = onNavigateBack,
        showCoins = true,
        coinsCount = wallet.coins,
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

                VoiceGameMainContent(state, viewModel)
            }
        }

        if (state.phase == VoiceGamePhase.EVALUATING) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                EvaluatingPhaseContent()
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

@Composable
fun VoiceGameMainContent(
    state: VoiceGameState,
    viewModel: VoiceGameViewModel
) {
    val context = LocalContext.current
    val isRecording = state.phase == VoiceGamePhase.RECORDING

    val resolvedTitle = when {
        isRecording -> stringResource(R.string.voice_recording_listening)
        else -> stringResource(R.string.voice_idle_yo_can_do_it)
    }

    val resolvedMascot = when {
        isRecording -> R.drawable.lingo_mic
        else -> R.drawable.lingo_initial_state_voice
    }
    Spacer(Modifier.height(50.dp))
    MessageBubble(title = resolvedTitle)

    AppMascotGradientBox(
        imageRes = resolvedMascot,
        mascotOverlapHeight = 70.dp,
        mascotSize = 180.dp
    ) {
        Text(
            text = stringResource(R.string.voice_idle_pronounce_this),
            style = AppTextStyles.Caption,
            color = LinguaQuestTheme.colors.iconsColor
        )
        Spacer(Modifier.height(8.dp))

        if (state.isLoadingSentence) {
            LingoSpinningIcon(size = 32.dp)
        } else {
            val isArabic = state.sentence.any {
                it in '\u0600'..'\u06FF' ||
                it in '\u0750'..'\u077F' ||
                it in '\u08A0'..'\u08FF' ||
                it in '\uFB50'..'\uFDFF' ||
                it in '\uFE70'..'\uFEFF'
            }
            Text(
                text = state.sentence,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textDirection = if (isArabic) TextDirection.Rtl else TextDirection.ContentOrLtr
                ),
                textAlign = TextAlign.Center,
                color = LinguaQuestTheme.colors.blackColor
            )
            state.phonetic?.let { phonetic ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = phonetic,
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    textAlign = TextAlign.Center,
                    color = LinguaQuestTheme.colors.iconsColor
                )
            }
            if (state.showTranslation && !state.translation.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.translation,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable(enabled = !state.isLoadingSentence && state.sentence.isNotBlank()) {
                    viewModel.onIntent(VoiceGameIntent.ListenClicked)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    tint = LinguaQuestTheme.colors.iconsColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.voice_idle_listen),
                    color = LinguaQuestTheme.colors.iconsColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(24.dp))

            Row(
                modifier = Modifier.clickable(enabled = !state.isLoadingSentence && !state.translation.isNullOrBlank()) {
                    viewModel.onIntent(VoiceGameIntent.ToggleTranslationClicked)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\uD83C\uDF10",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.mind_reader_translate),
                    color = LinguaQuestTheme.colors.iconsColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    if (isRecording) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.2f))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(LinguaQuestTheme.colors.ErrorAccent)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = formatElapsed(state.recordingElapsedSeconds),
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.ErrorAccent
            )
        }
    } else {
        Text(
            text = stringResource(R.string.voice_idle_tap_hold_record),
            color = LinguaQuestTheme.colors.iconsColor
        )
    }

    Spacer(Modifier.height(12.dp))

    PushToTalkButton(
        isRecording = isRecording,
        isEnabled = !state.isLoadingSentence && state.sentence.isNotBlank(),
        onPressStart = {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                viewModel.onIntent(VoiceGameIntent.MicPermissionGranted)
            } else {
                viewModel.onIntent(VoiceGameIntent.RecordClicked)
            }
        },
        onPressEnd = {
            viewModel.onIntent(VoiceGameIntent.DoneClicked)
        }
    )

    Spacer(Modifier.height(20.dp))

    if (!isRecording) {
        AppButton3D(
            text = stringResource(R.string.voice_idle_skip),
            onClick = { viewModel.onIntent(VoiceGameIntent.SkipClicked) },
            variant = ButtonVariant.SECONDARY
        )
    } else {
        AppButton3D(
            text = stringResource(R.string.voice_recording_cancel),
            onClick = { viewModel.onIntent(VoiceGameIntent.CancelRecordingClicked) },
            variant = ButtonVariant.SECONDARY
        )
    }
    Spacer(Modifier.height(16.dp))
}
