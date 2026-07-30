package com.iti.linguaquest.features.setting.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AboutAppScreen(
    onBack: () -> Unit,
    onFaqsClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    onUserGuideClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AboutAppContent(
        onBackClick = onBack,
        onFaqsClick = onFaqsClick,
        onContactUsClick = onContactUsClick,
        onUserGuideClick = onUserGuideClick,
        modifier = modifier
    )
}
