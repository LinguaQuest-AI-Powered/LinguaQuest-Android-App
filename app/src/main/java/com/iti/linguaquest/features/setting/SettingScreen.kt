package com.iti.linguaquest.features.setting

import androidx.compose.runtime.Composable

@Composable
fun SettingScreen(
    onBack: () -> Unit
) {
    SettingContent(
        onBackClick = onBack,
        onLogoutClick = {
         },
        onDeleteAccountClick = {
         }
    )
}
