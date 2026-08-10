package com.iti.linguaquest.features.roleplay.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.iti.linguaquest.core.sharedComponents.LingoSpinningIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.AppMascotGradientBox
import com.iti.linguaquest.core.sharedComponents.dialog.AppDialog
import com.iti.linguaquest.core.sharedComponents.PushToTalkButton
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.roleplay.domain.model.BossScenario
import com.iti.linguaquest.features.roleplay.domain.model.ChatMessage
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState

@Composable
fun ActiveLiveChatView(
    state: RoleplayState,
    isBossStage: Boolean,
    onStopRecording: () -> Unit,
    onRecord: () -> Unit,
    onFinishStage: () -> Unit,
    modifier: Modifier = Modifier
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
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))

        if (isBossStage && state.currentBossScenario != null) {
            RoleplayObjectiveBanner(
                scenario = state.currentBossScenario,
                isTimerRunning = state.isTimerRunning,
                remainingTimeSeconds = state.remainingTimeSeconds
            )
            Spacer(Modifier.height(12.dp))
        }

        val mascotImageRes = lingoImageForState(
            isAiSpeaking = state.isAiSpeaking,
            isUserSpeaking = state.isUserSpeaking,
            isLoading = state.isLoading,
            isAiThinking = state.isAiThinking
        )

        AppMascotGradientBox(
            imageRes = mascotImageRes,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            mascotSize = 140.dp,
            mascotOverlapHeight = 50.dp,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LingoSpinningIcon()
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.roleplay_connecting),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (!state.isConnected) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.roleplay_not_connected),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LinguaQuestTheme.colors.ErrorAccent
                    )
                }
            } else {
                val listState = rememberLazyListState()
                LaunchedEffect(
                    state.transcriptionHistory.size,
                    state.transcriptionHistory.lastOrNull()?.text?.length,
                    state.isAiThinking
                ) {
                    val totalItems = state.transcriptionHistory.size + if (state.isAiThinking) 1 else 0
                    if (totalItems > 0) {
                        listState.animateScrollToItem(totalItems - 1)
                    }
                }

                val gradientTransparent = LinguaQuestTheme.colors.whiteColor.copy(alpha = 0f)
                val gradientBlack = LinguaQuestTheme.colors.blackColor

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = 0.99f }
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    0f to gradientTransparent,
                                    0.04f to gradientBlack,
                                    0.96f to gradientBlack,
                                    1f to gradientTransparent
                                ),
                                blendMode = BlendMode.DstIn
                            )
                        },
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.transcriptionHistory) { message ->
                        RoleplayChatMessageBubble(message = message)
                    }
                    if (state.isAiThinking) {
                        item {
                            AiTypingIndicator()
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (state.isConnected) {
            val isMicEnabled = !state.isAiSpeaking
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PushToTalkButton(
                    isRecording = state.isUserSpeaking,
                    isEnabled = isMicEnabled,
                    onPressStart = { onRecord() },
                    onPressEnd = { onStopRecording() }
                )
                Spacer(Modifier.height(6.dp))
                val statusText = when {
                    state.isUserSpeaking -> stringResource(R.string.roleplay_release_to_send)
                    state.isAiSpeaking -> stringResource(R.string.roleplay_ai_speaking)
                    state.isAiThinking -> stringResource(R.string.roleplay_ai_thinking)
                    else -> stringResource(R.string.roleplay_hold_to_speak)
                }
                Text(
                    text = statusText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (isBossStage) {
                Spacer(Modifier.height(12.dp))
                AppButton3D(
                    onClick = { showFinishDialog = true },
                    backgroundColorOverride = LinguaQuestTheme.colors.ErrorAccent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = stringResource(R.string.roleplay_finish_stage)
                )
            } else {
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFF8F4)
@Composable
private fun ActiveLiveChatViewPreview() {
    LinguaQuestTheme {
        ActiveLiveChatView(
            state = RoleplayState(
                isConnected = true,
                isUserSpeaking = false,
                isAiSpeaking = false,
                isAiThinking = true,
                currentBossScenario = BossScenario(
                    id = ScenarioId.SCENARIO_MARKET_01,
                    bossName = "Sherry",
                    roleDescription = "Fruit Vendor",
                    objective = "Buy some fresh mangoes.",
                    worldId = "2",
                    voiceName = "KORE"
                ),
                transcriptionHistory = listOf(
                    ChatMessage("Hello! I am Sherry, what would you like?", isUser = false),
                    ChatMessage("I'd like to buy some mangoes please.", isUser = true)
                )
            ),
            isBossStage = true,
            onStopRecording = {},
            onRecord = {},
            onFinishStage = {}
        )
    }
}
