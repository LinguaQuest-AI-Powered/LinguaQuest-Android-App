package com.iti.linguaquest.features.roleplay.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.RoleplayAssessmentResult
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.LingoRoleplayAvatar
import com.iti.linguaquest.features.roleplay.presentation.view.components.PushToTalkButton
import com.iti.linguaquest.features.roleplay.presentation.view.components.RoleplayTopBar
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel
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
            }
        }
    }

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (state.currentBossScenario != null) {
                viewModel.onIntent(RoleplayIntent.StartBossStageClicked)
            } else {
                viewModel.onIntent(RoleplayIntent.StartLevelClicked)
            }
        }
    }

    // Auto-connect when landing on the screen ONLY if not a boss stage
    LaunchedEffect(state.currentBossScenario) {
        if (!state.isConnected && !state.isLoading && state.currentBossScenario == null && !state.isEvaluating) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                viewModel.onIntent(RoleplayIntent.StartLevelClicked)
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
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

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (state.currentBossScenario != null) {
                when {
                    state.assessmentResult != null -> {
                        BossResultScreen(state.assessmentResult!!, viewModel)
                    }
                    state.isEvaluating -> {
                        BossEvaluatingScreen()
                    }
                    state.isConnected || state.isLoading -> {
                        ActiveLiveChatScreen(state, viewModel, isBossStage = true)
                    }
                    else -> {
                        BossLobbyScreen(
                            scenario = state.currentBossScenario!!,
                            onStartClicked = {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                    viewModel.onIntent(RoleplayIntent.StartBossStageClicked)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        )
                    }
                }
            } else {
                ActiveLiveChatScreen(state, viewModel, isBossStage = false)
            }
        }
    }
}

@Composable
fun BossLobbyScreen(scenario: BossScenario, onStartClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Boss Stage",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Meet ${scenario.bossName}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = scenario.roleDescription,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Your Objective",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = scenario.taskObjective,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "(Read carefully in your native language before starting)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onStartClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Start Roleplay", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ActiveLiveChatScreen(state: RoleplayState, viewModel: RoleplayViewModel, isBossStage: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // Boss Objective Banner
        if (isBossStage && state.currentBossScenario != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Objective: ${state.currentBossScenario.taskObjective}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(16.dp))
        }

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
            Text("Connecting...", style = MaterialTheme.typography.bodyMedium)
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

        // Push to talk & Finish Button
        if (state.isConnected) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            
            if (isBossStage) {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.onIntent(RoleplayIntent.FinishStageClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = LinguaQuestTheme.colors.ErrorAccent),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text("Finish Stage", fontWeight = FontWeight.Bold)
                }
            } else {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BossEvaluatingScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "AI is analyzing your conversation...",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BossResultScreen(result: RoleplayAssessmentResult, viewModel: RoleplayViewModel) {
    val isSuccess = result.isTaskCompleted
    val title = if (isSuccess) "Victory!" else "Stage Failed"
    val titleColor = if (isSuccess) LinguaQuestTheme.colors.OrangeActive else LinguaQuestTheme.colors.ErrorAccent
    
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Fluency Score: ${result.fluencyScore}/100",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = result.feedbackMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isSuccess) {
                    Text(
                        text = "Rewards: +150 XP, +50 Coins",
                        style = MaterialTheme.typography.titleMedium,
                        color = LinguaQuestTheme.colors.OrangeActive,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Rewards: 0 XP",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (isSuccess) {
            Button(
                onClick = { viewModel.onIntent(RoleplayIntent.AdvanceToNextWorldClicked) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Next World", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = { viewModel.onIntent(RoleplayIntent.RetryStageClicked) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Try Again", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
