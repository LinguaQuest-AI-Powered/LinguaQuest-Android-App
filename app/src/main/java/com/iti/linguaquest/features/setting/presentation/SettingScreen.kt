package com.iti.linguaquest.features.setting.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
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
        onNotificationsToggle = viewModel::toggleNotifications,
        onLogoutClick = {},
        onEditProfileClick = onEdit,
        reminderState = reminderState,
        onReminderIntent = viewModel::onReminderIntent
    )
}
