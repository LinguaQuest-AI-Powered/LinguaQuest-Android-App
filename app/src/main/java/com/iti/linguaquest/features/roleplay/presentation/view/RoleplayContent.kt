package com.iti.linguaquest.features.roleplay.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.ActiveLiveChatScreen
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossEvaluatingScreen
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossLobbyScreen
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossResultScreen
import com.iti.linguaquest.features.roleplay.presentation.view.components.RoleplayTopBar

@Composable
fun RoleplayContent(
    state: RoleplayState,
    onIntent: (RoleplayIntent) -> Unit,
    onStartBossStage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(vertical = 25.dp)) {
        RoleplayTopBar(
            targetLanguage = state.targetLanguage,
            onExitClicked = {
                onIntent(RoleplayIntent.ReturnHomeClicked)
            }
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (state.currentBossScenario != null) {
                when {
                    state.assessmentResult != null -> {
                        BossResultScreen(
                            result = state.assessmentResult,
                            onAdvanceToNextWorld = { onIntent(RoleplayIntent.AdvanceToNextWorldClicked) },
                            onRetryStage = { onIntent(RoleplayIntent.RetryStageClicked) }
                        )
                    }
                    state.isEvaluating -> {
                        BossEvaluatingScreen()
                    }
                    state.isConnected || state.isLoading -> {
                        ActiveLiveChatScreen(
                            state = state, 
                            isBossStage = true,
                            onStopRecording = { onIntent(RoleplayIntent.StopRecordingClicked) },
                            onRecord = { onIntent(RoleplayIntent.RecordClicked) },
                            onFinishStage = { onIntent(RoleplayIntent.FinishStageClicked) }
                        )
                    }
                    else -> {
                        BossLobbyScreen(
                            scenario = state.currentBossScenario,
                            onStartClicked = onStartBossStage
                        )
                    }
                }
            } else {
                ActiveLiveChatScreen(
                    state = state, 
                    isBossStage = false,
                    onStopRecording = { onIntent(RoleplayIntent.StopRecordingClicked) },
                    onRecord = { onIntent(RoleplayIntent.RecordClicked) },
                    onFinishStage = { }
                )
            }
        }
    }
}
