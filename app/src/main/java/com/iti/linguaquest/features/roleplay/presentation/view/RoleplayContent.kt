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
import com.iti.linguaquest.features.roleplay.presentation.view.components.ActiveLiveChatView
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossEvaluatingView
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossLobbyView
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossResultView
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

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (state.currentBossScenario != null) {
                when {
                    state.assessmentResult != null -> {
                        BossResultView(
                            result = state.assessmentResult,
                            onAdvanceToNextWorld = { onIntent(RoleplayIntent.AdvanceToNextWorldClicked) },
                            onRetryStage = { onIntent(RoleplayIntent.RetryStageClicked) }
                        )
                    }
                    state.isEvaluating -> {
                        BossEvaluatingView()
                    }
                    state.isConnected || state.isLoading -> {
                        ActiveLiveChatView(
                            state = state, 
                            isBossStage = true,
                            onStopRecording = { onIntent(RoleplayIntent.StopRecordingClicked) },
                            onRecord = { onIntent(RoleplayIntent.RecordClicked) },
                            onFinishStage = { onIntent(RoleplayIntent.FinishStageClicked) }
                        )
                    }
                    else -> {
                        BossLobbyView(
                            scenario = state.currentBossScenario,
                            onStartClicked = onStartBossStage
                        )
                    }
                }
            } else {
                ActiveLiveChatView(
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
