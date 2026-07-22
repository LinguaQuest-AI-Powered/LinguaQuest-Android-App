package com.iti.linguaquest.features.lockscreen.presentation.view

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iti.linguaquest.R
import com.iti.linguaquest.core.theme.AppColors
import com.iti.linguaquest.core.theme.LinguaQuestTheme
import com.iti.linguaquest.core.sharedComponents.LinguaQuestScreenTopBar
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureState
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenEffect
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenIntent
import com.iti.linguaquest.features.lockscreen.presentation.viewmodel.LockScreenSettingsViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.DateFormat
import java.util.Date

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

                is LockScreenEffect.ShowMessage -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
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
                    ErrorCard(message = state.errorMessage.orEmpty())
                }

                val primaryLabel = when (state.featureState) {
                    LockScreenFeatureState.DISABLED -> stringResource(R.string.lockscreen_vocabulary_enable_action)
                    LockScreenFeatureState.ERROR -> stringResource(R.string.lockscreen_vocabulary_retry)
                    LockScreenFeatureState.ENABLING -> stringResource(R.string.lockscreen_vocabulary_state_generating)
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
            }
        }
    }
}

@Composable
private fun HeroCard(state: com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState) {
    val statusLabel = when (state.featureState) {
        LockScreenFeatureState.DISABLED -> stringResource(R.string.lockscreen_vocabulary_state_disabled)
        LockScreenFeatureState.ENABLING -> stringResource(R.string.lockscreen_vocabulary_state_generating)
        LockScreenFeatureState.ACTIVE -> stringResource(R.string.lockscreen_vocabulary_state_active)
        LockScreenFeatureState.DISABLING -> stringResource(R.string.lockscreen_vocabulary_state_generating)
        LockScreenFeatureState.ERROR -> stringResource(R.string.lockscreen_vocabulary_state_error)
    }

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = LinguaQuestTheme.colors.ChipBackground,
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock_icon),
                            contentDescription = null,
                            tint = AppColors.OrangeActive,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.lockscreen_vocabulary_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LinguaQuestTheme.colors.titleAndCationsColor
                    )
                    Text(
                        text = stringResource(R.string.lockscreen_vocabulary_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            StatusBadge(text = statusLabel)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniInfoChip(
                    label = stringResource(R.string.lockscreen_vocabulary_cost_chip),
                    iconRes = R.drawable.ic_coin
                )
                MiniInfoChip(
                    label = stringResource(R.string.lockscreen_vocabulary_schedule_chip),
                    iconRes = R.drawable.ic_timer
                )

            }
        }
    }
}

@Composable
private fun ToggleCard(
    state: com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState,
    onToggle: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.lockscreen_vocabulary_status_label),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = LinguaQuestTheme.colors.titleAndCationsColor
                )
                Text(
                    text = when {
                        state.featureState == LockScreenFeatureState.DISABLED -> stringResource(R.string.lockscreen_vocabulary_state_disabled)
                        state.pendingGeneration -> stringResource(R.string.lockscreen_vocabulary_state_generating)
                        state.featureState == LockScreenFeatureState.ERROR -> stringResource(R.string.lockscreen_vocabulary_state_error)
                        else -> stringResource(R.string.lockscreen_vocabulary_state_active)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = state.featureState != LockScreenFeatureState.DISABLED &&
                    state.featureState != LockScreenFeatureState.DISABLING,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AppColors.White,
                    checkedTrackColor = AppColors.OrangeActive,
                    uncheckedThumbColor = AppColors.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                )
            )
        }
    }
}

@Composable
private fun StatsCard(state: com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenState) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.lockscreen_vocabulary_status_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_pending_words),
                    value = state.pendingCount.toString(),
                    iconRes = R.drawable.ic_bell_icon
                )
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_batch_size),
                    value = state.batchSize.toString(),
                    iconRes = R.drawable.ic_coin
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_target_language),
                    value = state.currentTargetLanguage ?: "—",
                    iconRes = R.drawable.ic_learning_language
                )
                StatCardItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.lockscreen_vocabulary_level),
                    value = state.currentProficiencyLevel ?: "—",
                    iconRes = R.drawable.ic_timer
                )
            }

            StatCardItem(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.lockscreen_vocabulary_last_generated),
                value = state.lastGenerationTime?.let { formatTime(it) }
                    ?: stringResource(R.string.lockscreen_vocabulary_state_ready),
                iconRes = R.drawable.ic_info_icon
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = LinguaQuestTheme.colors.ErrorAccent.copy(alpha = 0.1f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message,
            color = LinguaQuestTheme.colors.ErrorAccent,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun StatusBadge(text: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = AppColors.ChipBackground,
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AppColors.OrangeActive)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        }
    }
}

@Composable
private fun MiniInfoChip(label: String, iconRes: Int) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = AppColors.OrangeActive,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        }
    }
}

@Composable
private fun StatCardItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    iconRes: Int
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = AppColors.OrangeActive,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LinguaQuestTheme.colors.titleAndCationsColor
            )
        }
    }
}

private fun formatTime(epochMillis: Long): String {
    return DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM,
        DateFormat.SHORT
    ).format(Date(epochMillis))
}
