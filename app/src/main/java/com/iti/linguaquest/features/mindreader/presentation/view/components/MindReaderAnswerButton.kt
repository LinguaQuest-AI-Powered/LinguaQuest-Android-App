package com.iti.linguaquest.features.mindreader.presentation.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.linguaquest.core.sharedComponents.AppButton3D
import com.iti.linguaquest.core.sharedComponents.ButtonVariant
import com.iti.linguaquest.core.theme.LinguaQuestTheme

@Composable
fun MindReaderAnswerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    AppButton3D(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = ButtonVariant.PRIMARY,
        enabled = enabled,
        backgroundColorOverride = LinguaQuestTheme.colors.MindReaderBeige,
        ledgeColorOverride = LinguaQuestTheme.colors.MindReaderBeige.copy(alpha = 0.7f),
        contentColorOverride = LinguaQuestTheme.colors.BrownText
    )
}
