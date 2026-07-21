package com.iti.linguaquest.features.setting

import androidx.compose.runtime.Composable

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onEdit:() -> Unit
)  {
    SettingContent(
        onEditProfileClick = onEdit,
        onBackClick = onBack,
        onLogoutClick = {
         },

    )
}
