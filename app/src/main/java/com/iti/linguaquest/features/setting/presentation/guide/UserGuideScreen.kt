package com.iti.linguaquest.features.setting.presentation.guide

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserGuideScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    UserGuideContent(
        onBackClick = onBack,
        modifier = modifier
    )
}
