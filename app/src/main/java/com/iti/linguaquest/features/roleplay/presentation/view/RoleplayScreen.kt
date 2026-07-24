package com.iti.linguaquest.features.roleplay.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.view.components.LingoRoleplayAvatar
import com.iti.linguaquest.features.roleplay.presentation.view.components.PushToTalkButton
import com.iti.linguaquest.features.roleplay.presentation.view.components.RoleplayTopBar
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel
import kotlinx.coroutines.flow.collectLatest

import androidx.compose.runtime.DisposableEffect

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
            }
        }
    }

    // Auto-connect when landing on the screen
    LaunchedEffect(Unit) {
        if (!state.isConnected && !state.isLoading) {
            viewModel.startRoleplay()
        }
    }

    // Clean up when screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            viewModel.endRoleplay()
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(vertical = 25.dp)) {
        RoleplayTopBar(
            targetLanguage = state.targetLanguage,
            onExitClicked = { 
                viewModel.endRoleplay()
                viewModel.onIntent(RoleplayIntent.ReturnHomeClicked) 
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Avatar Area
            LingoRoleplayAvatar(
                isAiSpeaking = state.isAiSpeaking,
                isUserSpeaking = state.isUserSpeaking,
                isLoading = state.isLoading,
                size = 140.dp
            )
            Spacer(Modifier.height(24.dp))

            // Loading state
            if (state.isLoading) {
                CircularProgressIndicator(color = LinguaQuestTheme.colors.OrangeActive)
                Spacer(Modifier.height(16.dp))
                Text("Connecting to Lingo...", style = MaterialTheme.typography.bodyMedium)
            } else if (!state.isConnected) {
                Text("Not connected.", style = MaterialTheme.typography.bodyMedium, color = LinguaQuestTheme.colors.ErrorAccent)
            }

            // Transcript Area
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.transcriptionHistory) { text ->
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomEnd = 16.dp,
                                        bottomStart = 4.dp
                                    )
                                )
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Push to talk button
            if (state.isConnected) {
                PushToTalkButton(
                    isRecording = state.isUserSpeaking,
                    onClick = {
                        if (state.isUserSpeaking) {
                            viewModel.onIntent(RoleplayIntent.StopRecordingClicked)
                        } else {
                            viewModel.onIntent(RoleplayIntent.RecordClicked)
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (state.isUserSpeaking) "Tap to Stop" else "Tap to Speak",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
