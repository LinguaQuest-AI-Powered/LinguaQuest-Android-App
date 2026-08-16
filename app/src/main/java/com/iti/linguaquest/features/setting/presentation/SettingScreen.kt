package com.iti.linguaquest.features.setting.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import android.app.Activity
import android.os.Build.VERSION_CODES.TIRAMISU
import com.iti.linguaquest.features.lockscreen.presentation.viewmodel.LockScreenSettingsViewModel
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenIntent
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.iti.linguaquest.features.lockscreen.presentation.contract.LockScreenEffect
import com.iti.linguaquest.core.sound.AppSound
import com.iti.linguaquest.core.sound.LocalSoundPlayer
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onLogout: () -> Unit,
    onLockScreenVocabularyClick: () -> Unit,
    onAboutAppClick: () -> Unit = {},
    onHelpSupportClick: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel(),
    lockScreenViewModel: LockScreenSettingsViewModel = hiltViewModel()
) {
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
    val isLoggingOut by viewModel.isLoggingOut.collectAsStateWithLifecycle()
    val reminderState by viewModel.reminderState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val availableLanguages by viewModel.availableLanguages.collectAsStateWithLifecycle()

    val lockScreenState by lockScreenViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val soundPlayer = LocalSoundPlayer.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        lockScreenViewModel.onIntent(LockScreenIntent.NotificationPermissionResult(granted))
    }

    LaunchedEffect(Unit) {
        val granted = if (Build.VERSION.SDK_INT < TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PermissionChecker.PERMISSION_GRANTED
        }
        lockScreenViewModel.onIntent(LockScreenIntent.SyncNotificationPermission(granted))
    }

    LaunchedEffect(Unit) {
        lockScreenViewModel.effect.collectLatest { effect ->
            when (effect) {
                LockScreenEffect.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        lockScreenViewModel.onIntent(
                            LockScreenIntent.NotificationPermissionResult(
                                true
                            )
                        )
                    }
                }
                LockScreenEffect.PlayCoinDeductedSound -> {
                    soundPlayer.play(AppSound.COIN)
                }

            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.languageChanged.collectLatest {
            if (Build.VERSION.SDK_INT < TIRAMISU) {
                (context as? Activity)?.recreate()
            }
        }
    }

    BackHandler(enabled = availableLanguages.isUpdatingLanguage) {
    }

    SettingContent(
        isOnline = isOnline,
        onBackClick = onBack,
        appLanguage = appLanguage,
        availableLanguagesState = availableLanguages,
        onRetryLanguages = viewModel::retryLoadLanguages,
        onChangeAppLanguage = viewModel::requestChangeAppLanguage,
        onConfirmChangeLanguage = viewModel::confirmChangeAppLanguage,
        onDismissChangeLanguageDialog = viewModel::dismissChangeLanguageDialog,
        appTheme = appTheme,
        onChangeAppTheme = viewModel::changeAppTheme,
        soundEnabled = soundEnabled,
        onSoundToggle = viewModel::toggleSound,
        notificationsEnabled = notificationsEnabled,
        onNotificationsToggle = viewModel::toggleNotifications,
        isLoggingOut = isLoggingOut,
        onLogoutClick = {
            viewModel.logout(onSuccess = onLogout)
        },
        onEditProfileClick = onEdit,
        onLockScreenVocabularyClick = onLockScreenVocabularyClick,
        onHelpSupportClick = onHelpSupportClick,
        onAboutAppClick = onAboutAppClick,
        onReplayAppTour = {
            viewModel.replayAppTour()
            onBack()
        },
        reminderState = reminderState,
        onReminderIntent = viewModel::onReminderIntent,
        lockScreenState = lockScreenState,
        onLockScreenIntent = lockScreenViewModel::onIntent
    )
}
