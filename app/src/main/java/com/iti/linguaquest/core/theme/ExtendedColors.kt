package com.iti.linguaquest.core.theme


import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Immutable
data class LinguaQuestColors(

    val textFieldFill: Color,
    val textFieldBorder: Color,
    val textFieldPlaceholder: Color,
    val iconsColor: Color,
    val splashTopLeftColor: Color,
    val splashBottomRightColor: Color,
    val socialButtonFill: Color,
    val socialButtonText: Color,
    val socialButtonBorder: Color,
    val titleAndCationsColor: Color
)

internal val LocalLinguaQuestColors =
    staticCompositionLocalOf<LinguaQuestColors> {
        error("No LinguaQuestColors provided")
    }

object LinguaQuestTheme {

    val colors: LinguaQuestColors
        @Composable
        @Stable
        get() = LocalLinguaQuestColors.current
}