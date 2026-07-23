package com.iti.linguaquest.features.setting.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onLogout: () -> Unit,
    onEdit: () -> Unit,
    onLockScreenVocabularyClick: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val appLanguage by viewModel.appLanguage.collectAsState()
    val appTheme by viewModel.appTheme.collectAsState()
    val soundEnabled by viewModel.soundEnabled.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val isLoggingOut by viewModel.isLoggingOut.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
    val reminderState by viewModel.reminderState.collectAsStateWithLifecycle()

    SettingContent(
        onBackClick = onBack,
        appLanguage = appLanguage,
        onChangeAppLanguage = viewModel::changeAppLanguage,
        appTheme = appTheme,
        onChangeAppTheme = viewModel::changeAppTheme,
        soundEnabled = soundEnabled,
        onSoundToggle = viewModel::toggleSound,
        notificationsEnabled = notificationsEnabled,
        onNotificationsToggle = { enabled ->
            viewModel.toggleNotifications(enabled)
        },
        isLoggingOut = isLoggingOut,
        onLogoutClick = {
            viewModel.logout(onSuccess = onLogout)
        },
        onEditProfileClick = onEdit,
        onNotificationsToggle = viewModel::toggleNotifications,
        onLogoutClick = {},
        onEditProfileClick = onEdit,
        onLockScreenVocabularyClick = onLockScreenVocabularyClick,
        reminderState = reminderState,
        onReminderIntent = viewModel::onReminderIntent
    )
}
