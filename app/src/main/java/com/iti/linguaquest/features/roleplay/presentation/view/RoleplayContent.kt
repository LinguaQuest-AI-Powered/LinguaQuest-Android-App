package com.iti.linguaquest.features.roleplay.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.iti.linguaquest.core.utils.formatCompact
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.theme.LocalLinguaQuestColors
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayState
import com.iti.linguaquest.features.roleplay.presentation.view.components.ActiveLiveChatView
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossEvaluatingView
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossLobbyView
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossResultView
import com.iti.linguaquest.features.roleplay.presentation.view.components.BossErrorView
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar

@Composable
fun RoleplayContent(
    state: RoleplayState,
    coins: Int,
    onIntent: (RoleplayIntent) -> Unit,
    onStartBossStage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        LinguaQuestScreenTopBar(
            title = stringResource(R.string.roleplay_boss_level),
            onBackClicked = { onIntent(RoleplayIntent.ReturnHomeClicked) },
            showDivider = true,
            showCoins = true,
            coinsCount = coins
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (state.error != null) {
                BossErrorView(
                    errorMessage = state.error,
                    onRetry = {
                        onIntent(RoleplayIntent.RetryStageClicked)
                    },
                    onExit = { onIntent(RoleplayIntent.ReturnHomeClicked) }
                )
            } else {
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        androidx.compose.material3.CircularProgressIndicator(color = com.iti.linguaquest.core.theme.LinguaQuestTheme.colors.OrangeActive)
                    }
                }
            }
        }
    }
}
