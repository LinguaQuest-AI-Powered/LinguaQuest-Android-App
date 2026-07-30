package com.iti.linguaquest.features.setting.presentation.faqs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FaqsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    FaqsContent(
        onBackClick = onBack,
        modifier = modifier
    )
}
