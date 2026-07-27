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
import com.iti.linguaquest.core.sharedComponents.AppButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.model.ChatMessage



@Composable
fun ActiveLiveChatView(
    state: RoleplayState, 
    isBossStage: Boolean,
    onStopRecording: () -> Unit,
    onRecord: () -> Unit,
    onFinishStage: () -> Unit
) {
    var showFinishDialog by remember { mutableStateOf(false) }

    if (showFinishDialog) {
        AppDialog(
            title = stringResource(R.string.roleplay_finish_stage_question),
            message = stringResource(R.string.roleplay_finish_stage_message),
            primaryButtonText = stringResource(R.string.roleplay_yes_evaluate),
            onPrimaryClick = {
                showFinishDialog = false
                onFinishStage()
            },
            secondaryButtonText = stringResource(R.string.roleplay_keep_talking),
            onSecondaryClick = { showFinishDialog = false }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))


        if (isBossStage && state.currentBossScenario != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.roleplay_objective_format, state.currentBossScenario.objective),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    
                    if (state.isTimerRunning) {
                        Spacer(Modifier.height(8.dp))
                        val minutes = state.remainingTimeSeconds / 60
                        val seconds = state.remainingTimeSeconds % 60
                        val timeString = String.format("%02d:%02d", minutes, seconds)
                        val timerColor = if (state.remainingTimeSeconds <= 30) LinguaQuestTheme.colors.ErrorAccent else MaterialTheme.colorScheme.onPrimaryContainer
                        
                        Text(
                            text = timeString,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = timerColor
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }


        LingoRoleplayAvatar(
            isAiSpeaking = state.isAiSpeaking,
            isUserSpeaking = state.isUserSpeaking,
            isLoading = state.isLoading,
            size = 140.dp
        )
        Spacer(Modifier.height(24.dp))


        if (state.isLoading) {
            CircularProgressIndicator(color = LinguaQuestTheme.colors.OrangeActive)
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.roleplay_connecting), style = MaterialTheme.typography.bodyMedium)
        } else if (!state.isConnected) {
            Text(stringResource(R.string.roleplay_not_connected), style = MaterialTheme.typography.bodyMedium, color = LinguaQuestTheme.colors.ErrorAccent)
        }


        val listState = androidx.compose.foundation.lazy.rememberLazyListState()
        androidx.compose.runtime.LaunchedEffect(state.transcriptionHistory.size, state.transcriptionHistory.lastOrNull()?.text?.length) {
            if (state.transcriptionHistory.isNotEmpty()) {
                listState.animateScrollToItem(state.transcriptionHistory.size - 1)
            }
        }

        val gradientTransparent = LinguaQuestTheme.colors.whiteColor.copy(alpha = 0f)
        val gradientBlack = LinguaQuestTheme.colors.blackColor
        
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to gradientTransparent,
                            0.05f to gradientBlack,
                            0.95f to gradientBlack,
                            1f to gradientTransparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                },
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.transcriptionHistory) { message ->
                if (!message.isUser) {
                    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp)
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Box(
                            modifier = Modifier
                                .shadow(2.dp, shape)
                                .clip(shape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(16.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = message.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 4.dp, bottomStart = 16.dp)
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Box(
                            modifier = Modifier
                                .shadow(2.dp, shape)
                                .clip(shape)
                                .background(LinguaQuestTheme.colors.OrangeActive)
                                .padding(16.dp)
                                .widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = message.text,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = LinguaQuestTheme.colors.whiteColor
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))


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
                        text = if (state.isUserSpeaking) stringResource(R.string.roleplay_tap_to_stop) else stringResource(R.string.roleplay_tap_to_speak),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            if (isBossStage) {
                Spacer(Modifier.height(24.dp))
                AppButton(
                    onClick = { showFinishDialog = true },
                    backgroundColorOverride = LinguaQuestTheme.colors.ErrorAccent,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    text = stringResource(R.string.roleplay_finish_stage)
                )
            } else {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFF8F4)
@Composable
fun ActiveLiveChatViewPreview() {
    LinguaQuestTheme {
        ActiveLiveChatView(
            state = RoleplayState(
                isConnected = true,
                isUserSpeaking = false,
                isAiSpeaking = false,
                currentBossScenario = BossScenario(
                    id = ScenarioId.SCENARIO_MARKET_01,
                    bossName = "Sherry",
                    roleDescription = "Fruit Vendor",
                    objective = "Buy some fresh mangoes.",
                    worldId = "2",
                    voiceName = "KORE"
                ),
                transcriptionHistory = listOf(
                    ChatMessage("[SPOKEN] Hello! I am Sherry, what would you like? [/SPOKEN]", isUser = false),
                    ChatMessage("I'd like to buy some mangoes please.", isUser = true),
                    ChatMessage("Hmm, let me see... [SPOKEN] Sure! We have fresh mangoes today. [/SPOKEN]", isUser = false)
                )
            ),
            isBossStage = true,
            onStopRecording = {},
            onRecord = {},
            onFinishStage = {}
        )
    }
}
