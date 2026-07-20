package com.iti.linguaquest.features.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val appLanguage by viewModel.appLanguage.collectAsState()
    val soundEnabled by viewModel.soundEnabled.collectAsState()

    SettingContent(
        onBackClick = onBack,
        appLanguage = appLanguage,
        onChangeAppLanguage = { language ->
            viewModel.changeAppLanguage(language)
        },
        soundEnabled = soundEnabled,
        onSoundToggle = { enabled ->
            viewModel.toggleSound(enabled)
        },
        onLogoutClick = {
         },
        onDeleteAccountClick = {
         }
    )
}
