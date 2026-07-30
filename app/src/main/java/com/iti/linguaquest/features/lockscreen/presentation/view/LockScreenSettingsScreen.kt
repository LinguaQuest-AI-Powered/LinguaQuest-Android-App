package com.iti.linguaquest.features.lockscreen.presentation.view

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenEffect
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenIntent
import com.iti.linguaquest.features.lockscreen.presentation.view.component.ErrorCard
import com.iti.linguaquest.features.lockscreen.presentation.view.component.HeroCard
import com.iti.linguaquest.features.lockscreen.presentation.view.component.StatsCard
import com.iti.linguaquest.features.lockscreen.presentation.view.component.ToggleCard
import com.iti.linguaquest.features.lockscreen.presentation.viewmodel.LockScreenSettingsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LockScreenSettingsScreen(
    onBack: () -> Unit,
    viewModel: LockScreenSettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onIntent(LockScreenIntent.NotificationPermissionResult(granted))
    }

    LaunchedEffect(Unit) {
        val granted = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PermissionChecker.PERMISSION_GRANTED
        }
        viewModel.onIntent(LockScreenIntent.SyncNotificationPermission(granted))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                LockScreenEffect.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.onIntent(LockScreenIntent.NotificationPermissionResult(true))
                    }
                }
            }
        }
    }

    if (state.isConfirmDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(LockScreenIntent.CancelEnableClicked) },
            title = { Text(stringResource(R.string.lockscreen_vocabulary_enable_title)) },
            text = { Text(stringResource(R.string.lockscreen_vocabulary_enable_message)) },
            confirmButton = {
                Button(onClick = { viewModel.onIntent(LockScreenIntent.ConfirmEnableClicked) }) {
                    Text(stringResource(R.string.lockscreen_vocabulary_enable_action))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.onIntent(LockScreenIntent.CancelEnableClicked) }) {
                    Text(stringResource(R.string.lockscreen_vocabulary_cancel_action))
                }
            }
        )
    }

    val backgroundBrush = Brush.verticalGradient(
        listOf(
            LinguaQuestTheme.colors.Amber.copy(alpha = 0.22f),
            MaterialTheme.colorScheme.background,
            LinguaQuestTheme.colors.ChipBackground.copy(alpha = 0.25f)
        )
    )

    Scaffold(
        topBar = {
            LinguaQuestScreenTopBar(
                title = stringResource(R.string.lockscreen_vocabulary_title),
                onBackClicked = onBack,
                modifier = Modifier
                    .statusBarsPadding()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.92f)),
                titleColor = LinguaQuestTheme.colors.BrownText
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeroCard(state = state)
                ToggleCard(state = state, onToggle = {
                    viewModel.onIntent(LockScreenIntent.ToggleFeatureClicked(it))
                })
                StatsCard(state = state)

                if (state.errorMessage != null) {
                    ErrorCard(message = state.errorMessage!!.asString(context))
                }

                val primaryLabel = when (state.featureState) {
                    LockScreenFeatureState.DISABLED -> stringResource(R.string.lockscreen_vocabulary_enable_action)
                    LockScreenFeatureState.ERROR -> stringResource(R.string.lockscreen_vocabulary_retry)
                    LockScreenFeatureState.ENABLING,
                    LockScreenFeatureState.DISABLING -> stringResource(R.string.lockscreen_vocabulary_state_generating)
                    LockScreenFeatureState.ACTIVE -> stringResource(R.string.lockscreen_vocabulary_retry)
                }

                Button(
                    onClick = {
                        if (state.featureState == LockScreenFeatureState.DISABLED) {
                            viewModel.onIntent(LockScreenIntent.ToggleFeatureClicked(true))
                        } else {
                            viewModel.onIntent(LockScreenIntent.RetryClicked)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.OrangeActive,
                        contentColor = AppColors.White
                    )
                ) {
                    Text(primaryLabel, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = { viewModel.onIntent(LockScreenIntent.DisableClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.featureState != LockScreenFeatureState.DISABLED,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(stringResource(R.string.lockscreen_vocabulary_disable))
                }

                if (state.featureState == LockScreenFeatureState.ACTIVE) {
                    OutlinedButton(
                        onClick = { viewModel.onIntent(LockScreenIntent.TestNotificationClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(stringResource(R.string.lockscreen_vocabulary_test_notification))
                    }
                }
            }
        }
    }
}
