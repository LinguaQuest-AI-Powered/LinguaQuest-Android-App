package com.iti.linguaquest.features.roleplay.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RoleplayScreen(
    scenarioId: String? = null,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoleplayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (scenarioId != null || state.currentBossScenario != null) {
                viewModel.onIntent(RoleplayIntent.StartBossStageClicked)
            } else {
                viewModel.onIntent(RoleplayIntent.StartLevelClicked)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                RoleplayEffect.NavigateToHome -> onNavigateHome()
            }
        }
    }

    // Single source of truth for initialization
    LaunchedEffect(scenarioId) {
        if (scenarioId != null) {
            viewModel.onIntent(RoleplayIntent.LoadBossLobby(scenarioId))
        } else {
            // Auto-connect when landing on the screen ONLY if not a boss stage (scenarioId == null)
            if (!state.isConnected && !state.isLoading && !state.isEvaluating) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    viewModel.onIntent(RoleplayIntent.StartLevelClicked)
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        }
    }

    // Clean up when screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            viewModel.endRoleplay()
        }
    }

    RoleplayContent(
        state = state,
        onIntent = viewModel::onIntent,
        onStartBossStage = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                viewModel.onIntent(RoleplayIntent.StartBossStageClicked)
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        },
        modifier = modifier
    )
}
