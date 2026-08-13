package com.iti.linguaquest.core.theme

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

    secondary = AppColors.DarkSecondaryColor,
    onSecondary = AppColors.DarkTextPrimary,

    tertiary = AppColors.DarkTeal,

    background = AppColors.DarkBackground,
    surface = AppColors.DarkSurface,

    onBackground = AppColors.DarkTextPrimary,
    onSurface = AppColors.DarkTextPrimary,

    outline = AppColors.DarkBorderColor,

    error = AppColors.Red
)

private val LightExtraColors = LinguaQuestColors(
    isDark = false,
    textFieldFill = AppColors.Background,
    textFieldBorder = AppColors.TextFieldBorderColor,
    textFieldPlaceholder = AppColors.TextFieldPlaceholderColor,

    socialButtonFill = AppColors.Background,
    socialButtonText = AppColors.TextOnSocialButton,
    iconsColor = AppColors.IconsColor,

    splashTopLeftColor = AppColors.SplashTopLeftColor,
    splashBottomRightColor = AppColors.SplashBottomRightColor,
    splashBackgroundSolid = AppColors.SplashBackgroundSolid,

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
    fieldCardBackground = AppColors.FieldCardBackground,
    Charcoal = AppColors.Charcoal,
    Espresso = AppColors.Espresso,
    Amber = AppColors.Amber,
    Linen = AppColors.Linen,
    Sand = AppColors.Sand,
    SuccessAccent = AppColors.SuccessAccent,
    ErrorAccent = AppColors.ErrorAccent,
    InfoAccent = AppColors.InfoAccent,
    LeaderboardGold = AppColors.LeaderboardGold,
    LeaderboardBlue = AppColors.LeaderboardBlue,
    LeaderboardBronze = AppColors.LeaderboardBronze,
    AchievementCyanBackground = AppColors.AchievementCyanBackground,
    AchievementCyanText = AppColors.AchievementCyanText,
    AchievementCardBorder = AppColors.AchievementCardBorder,
    AchievementDivider = AppColors.AchievementDivider,
    AchievementButtonShadow = AppColors.AchievementButtonShadow,
    AchievementTabInactive = AppColors.AchievementTabInactive,
    AchievementHeaderGradientTop = AppColors.AchievementHeaderGradientTop,
    AchievementHeaderGradientBottom = AppColors.AchievementHeaderGradientBottom,
    DialogGradientTopRight = AppColors.DialogGradientTopRight,
    DialogGradientBottomLeft = AppColors.DialogGradientBottomLeft,
    ShadowOrange = AppColors.ShadowOrange,

    MapPathOuter = AppColors.MapPathOuter,
    MapPathInner = AppColors.MapPathInner,
    MapPathDash = AppColors.MapPathDash,

    DailyRewardInactiveNodeBg = AppColors.DailyRewardInactiveNodeBg,
    DailyRewardInactiveNodeIcon = AppColors.DailyRewardInactiveNodeIcon,
    DailyRewardBadgeBg = AppColors.DailyRewardBadgeBg,
    DailyRewardBadgeText = AppColors.DailyRewardBadgeText,
    DailyRewardActiveText = AppColors.DailyRewardActiveText,
    DailyRewardSubtitleText = AppColors.DailyRewardSubtitleText,
    DailyRewardInactiveLine = AppColors.DailyRewardInactiveLine,
    DailyRewardInactiveText = AppColors.DailyRewardInactiveText,
    MindReaderBeige = AppColors.MindReaderBeige,
    MindReaderCream = AppColors.MindReaderCream,
    CameraKnobCoral = AppColors.CameraKnobCoralLight,
    CameraKnobCoralHighlight = AppColors.CameraKnobCoralHighlightLight,
    NotificationAchievementBg = AppColors.NotificationAchievementLight,
    NotificationStreakBg = AppColors.NotificationStreakLight,
    NotificationDailyRewardBg = AppColors.NotificationDailyRewardLight,
    NotificationDailyMissionBg = AppColors.NotificationDailyMissionLight
)

private val DarkExtraColors = LinguaQuestColors(
    isDark = true,
    textFieldFill = AppColors.DarkBackground,
    textFieldBorder = AppColors.DarkBorderColor,
    textFieldPlaceholder = AppColors.DarkTextSecondary.copy(alpha = 0.5f),

    socialButtonFill = AppColors.DarkSurface,
    socialButtonText = AppColors.DarkTextPrimary,
    iconsColor = AppColors.DarkTextPrimary,
    splashTopLeftColor = AppColors.SplashTopLeftColor,
    splashBottomRightColor = AppColors.SplashBottomRightColor,
    splashBackgroundSolid = AppColors.SplashBackgroundSolid,
    socialButtonBorder = AppColors.DarkBorderColor,
    titleAndCationsColor = AppColors.DarkTextPrimary,
    blackColor = AppColors.DarkTextPrimary,
    whiteColor = AppColors.DarkSurface,
    cardLevelFilledColor = AppColors.DarkSecondaryColor,
    progressTrackRemainedColor = AppColors.DarkBorderColor,
    IconBoxBackground = AppColors.DarkSecondaryColor,
    ChipBackground = AppColors.DarkSecondaryColor,
    OrangeActive = AppColors.OrangeActive,
    BrownText = AppColors.DarkTextPrimary,
    ProfileCardColor = AppColors.DarkSurface,
    ProfileCardBorderColor = AppColors.DarkBorderColor,
    Charcoal = AppColors.Linen,
    Espresso = AppColors.Sand,
    fieldCardBackground = AppColors.DarkSurface,
    Amber = AppColors.Amber,
    Linen = AppColors.Charcoal,
    Sand = AppColors.Espresso,
    SuccessAccent = AppColors.SuccessAccent,
    ErrorAccent = AppColors.ErrorAccent,
    InfoAccent = AppColors.InfoAccent,
    LeaderboardGold = AppColors.DarkLeaderboardGold,
    LeaderboardBlue = AppColors.DarkLeaderboardBlue,
    LeaderboardBronze = AppColors.DarkLeaderboardBronze,
    AchievementCyanBackground = AppColors.DarkAchievementCyanBackground,
    AchievementCyanText = AppColors.DarkAchievementCyanText,
    AchievementCardBorder = AppColors.DarkAchievementCardBorder,
    AchievementDivider = AppColors.DarkAchievementDivider,
    AchievementButtonShadow = AppColors.DarkAchievementButtonShadow,
    AchievementTabInactive = AppColors.DarkAchievementTabInactive,
    AchievementHeaderGradientTop = AppColors.DarkAchievementHeaderGradientTop,
    AchievementHeaderGradientBottom = AppColors.DarkAchievementHeaderGradientBottom,
    DialogGradientTopRight = AppColors.DarkDialogGradientTopRight,
    DialogGradientBottomLeft = AppColors.DarkDialogGradientBottomLeft,
    ShadowOrange = AppColors.DarkShadowOrange,

    MapPathOuter = AppColors.DarkMapPathOuter,
    MapPathInner = AppColors.DarkMapPathInner,
    MapPathDash = AppColors.DarkMapPathDash,

    DailyRewardInactiveNodeBg = AppColors.DarkDailyRewardInactiveNodeBg,
    DailyRewardInactiveNodeIcon = AppColors.DarkDailyRewardInactiveNodeIcon,
    DailyRewardBadgeBg = AppColors.DarkDailyRewardBadgeBg,
    DailyRewardBadgeText = AppColors.DarkDailyRewardBadgeText,
    DailyRewardActiveText = AppColors.DarkDailyRewardActiveText,
    DailyRewardSubtitleText = AppColors.DarkDailyRewardSubtitleText,
    DailyRewardInactiveLine = AppColors.DarkDailyRewardInactiveLine,
    DailyRewardInactiveText = AppColors.DarkDailyRewardInactiveText,
    MindReaderBeige = AppColors.DarkMindReaderBeige,
    MindReaderCream = AppColors.DarkMindReaderCream,
    CameraKnobCoral = AppColors.CameraKnobCoralDark,
    CameraKnobCoralHighlight = AppColors.CameraKnobCoralHighlightDark,
    NotificationAchievementBg = AppColors.NotificationAchievementDark,
    NotificationStreakBg = AppColors.NotificationStreakDark,
    NotificationDailyRewardBg = AppColors.NotificationDailyRewardDark,
    NotificationDailyMissionBg = AppColors.NotificationDailyMissionDark
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