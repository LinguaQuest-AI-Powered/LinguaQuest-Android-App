package com.iti.linguaquest.features.roleplay.presentation.view

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayPhase
import com.iti.linguaquest.features.roleplay.presentation.view.components.RoleplayTopBar
import com.iti.linguaquest.features.roleplay.presentation.view.contents.AiSpeakingPhaseContent
import com.iti.linguaquest.features.roleplay.presentation.view.contents.IdlePhaseContent
import com.iti.linguaquest.features.roleplay.presentation.view.contents.LobbyPhaseContent
import com.iti.linguaquest.features.roleplay.presentation.view.contents.OutcomePhaseContent
import com.iti.linguaquest.features.roleplay.presentation.view.contents.ProcessingPhaseContent
import com.iti.linguaquest.features.roleplay.presentation.view.contents.RecordingPhaseContent
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RoleplayScreen(
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoleplayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                RoleplayEffect.NavigateToHome -> onNavigateHome()

                is RoleplayEffect.PlayAiAudio -> {
                    // TODO: Replace with real audio player when implemented.
                    //  For now, simulate playback duration then signal completion.
                    delay(2000L)
                    viewModel.onIntent(RoleplayIntent.AiAudioFinished)
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(vertical = 25.dp)) {
        RoleplayTopBar(
            targetLanguage = state.targetLanguage,
            onExitClicked = onNavigateHome
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
                RoleplayPhase.LOBBY -> LobbyPhaseContent(state, viewModel)
                RoleplayPhase.IDLE -> IdlePhaseContent(state, viewModel)
                RoleplayPhase.RECORDING -> RecordingPhaseContent(state, viewModel)
                RoleplayPhase.PROCESSING -> ProcessingPhaseContent(state, viewModel)
                RoleplayPhase.AI_SPEAKING -> AiSpeakingPhaseContent(state, viewModel)
                RoleplayPhase.OUTCOME -> OutcomePhaseContent(state, viewModel)
            }
        }
    }
}
