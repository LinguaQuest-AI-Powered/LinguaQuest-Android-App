package com.iti.linguaquest.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.iti.linguaquest.core.theme.AppColors

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

    background = AppColors.BackGround,
    surface = AppColors.BackGround,

    onBackground = AppColors.Brown,
    onSurface = AppColors.Brown,

    outline = AppColors.SocialBorderColor,

    error = AppColors.Red
)

private val LightExtraColors = LinguaQuestColors(

    textFieldFill = AppColors.BackGround,
    textFieldBorder = AppColors.TextFieldBorderColor,
    textFieldPlaceholder = AppColors.TextFieldPlaceholderColor,

    socialButtonFill = AppColors.BackGround,
    socialButtonText = AppColors.TextOnSocialButton,
    iconsColor = AppColors.IconsColor,

    splashTopLeftColor = AppColors.SplashTopLeftColor,
    splashBottomRightColor = AppColors.SplashBottomRightColor,

    socialButtonBorder = AppColors.SocialBorderColor,
    titleAndCationsColor = AppColors.TitleAndCaptionColor,
    blackColor = AppColors.Black,
    whiteColor = AppColors.White,
    cardLevelFilledColor = AppColors.cardLevelFilledColor,
    progressTrackRemainedColor = AppColors.progressTrackColor,
    IconBoxBackground = AppColors.IconBoxBackground,
    ChipBackground = AppColors.ChipBackground,
    OrangeActive = AppColors.OrangeActive,
    BrownText = AppColors.BrownText,
    ProfileCardColor = AppColors.ProfileCardColor,
    ProfileCardBorderColor = AppColors.ProfileCardBorderColor,
    Charcoal = AppColors.Charcoal,
    Espresso = AppColors.Espresso,
    Amber = AppColors.Amber,
    Linen = AppColors.Linen,
    Sand = AppColors.Sand,
    SuccessAccent = AppColors.SuccessAccent,
    ErrorAccent = AppColors.ErrorAccent,
    InfoAccent = AppColors.InfoAccent
)

private val DarkExtraColors = LinguaQuestColors(

    textFieldFill = AppColors.BackGround,
    textFieldBorder = AppColors.TextFieldBorderColor,
    textFieldPlaceholder = AppColors.TextFieldPlaceholderColor,

    socialButtonFill = AppColors.BackGround,
    socialButtonText = AppColors.TextOnSocialButton,
    iconsColor = AppColors.IconsColor,
    splashTopLeftColor = AppColors.SplashTopLeftColor,
    splashBottomRightColor = AppColors.SplashBottomRightColor,
    socialButtonBorder = AppColors.SocialBorderColor,
    titleAndCationsColor = AppColors.TitleAndCaptionColor,
    blackColor = AppColors.Black,
    whiteColor = AppColors.White,
    cardLevelFilledColor = AppColors.cardLevelFilledColor,
    progressTrackRemainedColor = AppColors.progressTrackColor,
    IconBoxBackground = AppColors.IconBoxBackground,
    ChipBackground = AppColors.ChipBackground,
    OrangeActive = AppColors.OrangeActive,
    BrownText = AppColors.BrownText,
    ProfileCardColor = AppColors.ProfileCardColor,
    ProfileCardBorderColor = AppColors.ProfileCardBorderColor,
    Charcoal = AppColors.Charcoal,
    Espresso = AppColors.Espresso,
    Amber = AppColors.Amber,
    Linen = AppColors.Linen,
    Sand = AppColors.Sand,
    SuccessAccent = AppColors.SuccessAccent,
    ErrorAccent = AppColors.ErrorAccent,
    InfoAccent = AppColors.InfoAccent,
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