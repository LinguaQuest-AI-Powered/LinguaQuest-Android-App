package com.iti.linguaquest.features.roleplay.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.offline.OfflineAwareContent
import com.iti.linguaquest.features.roleplay.domain.model.ScenarioId
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayEffect
import com.iti.linguaquest.features.roleplay.presentation.contract.RoleplayIntent
import com.iti.linguaquest.features.roleplay.presentation.viewModel.RoleplayViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RoleplayScreen(
    scenarioId: ScenarioId,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoleplayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val wallet by viewModel.wallet.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (state.currentBossScenario != null) {
                viewModel.onIntent(RoleplayIntent.StartBossStageClicked)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                RoleplayEffect.NavigateToHome -> onNavigateHome()
                is RoleplayEffect.ShowSnackbarAndNavigateBack -> {
                    onNavigateHome()
                }
            }
        }
    }


    LaunchedEffect(scenarioId) {
        viewModel.onIntent(RoleplayIntent.LoadBossLobby(scenarioId))
    }


    DisposableEffect(Unit) {
        onDispose {
            viewModel.endRoleplay()
        }
    }

    OfflineAwareContent(
        isOnline = isOnline,
        topBarTitle = stringResource(R.string.roleplay_boss_level),
        onBackClicked = { viewModel.onIntent(RoleplayIntent.ReturnHomeClicked) },
        showCoins = true,
        coinsCount = wallet.coins,
        modifier = modifier.fillMaxSize()
    ) {
        RoleplayContent(
            state = state,
            coins = wallet.coins,
            onIntent = viewModel::onIntent,
            onStartBossStage = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    viewModel.onIntent(RoleplayIntent.StartBossStageClicked)
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
