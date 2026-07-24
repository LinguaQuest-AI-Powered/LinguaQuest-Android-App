package com.iti.linguaquest.features.roleplay.presentation.view.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState

@Composable
fun ActiveLiveChatScreen(
    state: RoleplayState, 
    isBossStage: Boolean,
    onStopRecording: () -> Unit,
    onRecord: () -> Unit,
    onFinishStage: () -> Unit
) {
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
                                onStopRecording()
                            } else {
                                onRecord()
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
                    onClick = onFinishStage,
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
