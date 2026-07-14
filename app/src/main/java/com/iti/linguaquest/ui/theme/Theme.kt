package com.iti.linguaquest.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(

    primary = AppColors.PrimaryColor,
    onPrimary = AppColors.TextOnPrimaryButton,

    secondary = AppColors.SecondaryColor,
    onSecondary = AppColors.TextOnSecondaryButton,

    tertiary = AppColors.Teal,

    background = AppColors.Background,
    surface = AppColors.Background,

    onBackground = AppColors.Brown,
    onSurface = AppColors.Brown,

    outline = AppColors.SocialBorderColor,

    error = AppColors.Red
)

private val DarkColorScheme = darkColorScheme(

    primary = AppColors.PrimaryColor,
    onPrimary = AppColors.TextOnPrimaryButton,

    secondary = AppColors.SecondaryColor,
    onSecondary = AppColors.TextOnSecondaryButton,

    tertiary = AppColors.Teal,

    background = AppColors.SocialButtonFillColor,
    surface = AppColors.SocialButtonFillColor,

    onBackground = AppColors.Brown,
    onSurface = AppColors.Brown,

    outline = AppColors.SocialBorderColor,

    error = AppColors.Red
)

private val LightExtraColors = LinguaQuestColors(

    textFieldFill = AppColors.TextFieldFillColor,
    textFieldBorder = AppColors.TextFieldBorderColor,
    textFieldPlaceholder = AppColors.TextFieldPlaceholderColor,

    socialButtonFill = AppColors.SocialButtonFillColor,
    socialButtonText = AppColors.TextOnSocialButton,
    iconsColor = AppColors.IconsColor,
    socialButtonBorder = AppColors.SocialBorderColor
)

private val DarkExtraColors = LinguaQuestColors(

    textFieldFill = AppColors.TextFieldFillColor,
    textFieldBorder = AppColors.TextFieldBorderColor,
    textFieldPlaceholder = AppColors.TextFieldPlaceholderColor,

    socialButtonFill = AppColors.SocialButtonFillColor,
    socialButtonText = AppColors.TextOnSocialButton,
    iconsColor = AppColors.IconsColor,
    socialButtonBorder = AppColors.SocialBorderColor
)

@Composable
fun LinguaQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val materialColors =
        if (darkTheme) DarkColorScheme else LightColorScheme

    val extraColors =
        if (darkTheme) DarkExtraColors else LightExtraColors

    CompositionLocalProvider(
        LocalLinguaQuestColors provides extraColors
    ) {

        MaterialTheme(
            colorScheme = materialColors,
            typography = AppTypography,
            content = content
        )
    }
}