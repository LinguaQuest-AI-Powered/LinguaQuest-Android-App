package com.iti.linguaquest.features.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val appLanguage by viewModel.appLanguage.collectAsState()
    val context = LocalContext.current

    SettingContent(
        onBackClick = onBack,
        appLanguage = appLanguage,
        onChangeAppLanguage = { language ->
            viewModel.changeAppLanguage(context, language)
        },
        onLogoutClick = {
         },
        onDeleteAccountClick = {
         }
    )
}
