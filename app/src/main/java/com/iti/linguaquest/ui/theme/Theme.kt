package com.iti.linguaquest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(

    primary = AppColors.PrimaryColor,
    onPrimary = AppColors.TextOnPrimaryButton,

    secondary = AppColors.SecondaryColor,
    onSecondary = AppColors.TextOnSecondaryButton,

    tertiary = AppColors.Teal,

    background = AppColors.SocialButtonFillColor,
    surface = AppColors.SocialButtonFillColor,

    onBackground = AppColors.Brown,
    onSurface = AppColors.Brown,

    error = AppColors.PrimaryColor
)

private val DarkColorScheme = darkColorScheme(

    // Temporary: same colors as light theme
    primary = AppColors.PrimaryColor,
    onPrimary = AppColors.TextOnPrimaryButton,

    secondary = AppColors.SecondaryColor,
    onSecondary = AppColors.TextOnSecondaryButton,

    tertiary = AppColors.Teal,

    background = AppColors.SocialButtonFillColor,
    surface = AppColors.SocialButtonFillColor,

    onBackground = AppColors.Brown,
    onSurface = AppColors.Brown,

    error = AppColors.PrimaryColor
)

@Composable
fun LinguaQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}