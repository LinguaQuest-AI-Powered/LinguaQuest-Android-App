package com.iti.linguaquest.core.theme


import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Immutable
data class LinguaQuestColors(
    val isDark: Boolean,
    val textFieldFill: Color,
    val textFieldBorder: Color,
    val textFieldPlaceholder: Color,
    val iconsColor: Color,
    val splashTopLeftColor: Color,
    val splashBottomRightColor: Color,
    val socialButtonFill: Color,
    val socialButtonText: Color,
    val socialButtonBorder: Color,
    val titleAndCationsColor: Color,
    val blackColor: Color,
    val whiteColor: Color,
    val cardLevelFilledColor: Color,
    val progressTrackRemainedColor: Color,
    val IconBoxBackground: Color,
    val ChipBackground: Color,
    val OrangeActive: Color,
    val BrownText: Color,
    val ProfileCardColor: Color,
    val ProfileCardBorderColor: Color,
    val Charcoal : Color,
    val Espresso : Color,
    val Amber : Color,
    val Linen : Color,
    val Sand : Color,
    val SuccessAccent : Color,
    val ErrorAccent : Color,
    val fieldCardBackground: Color,
    val InfoAccent : Color,
    val LeaderboardGold: Color,
    val LeaderboardBlue: Color,
    val LeaderboardBronze: Color,
    val AchievementCyanBackground: Color,
    val AchievementCyanText: Color,
    val AchievementCardBorder: Color,
    val AchievementDivider: Color,
    val AchievementButtonShadow: Color,
    val AchievementTabInactive: Color,
    val AchievementHeaderGradientTop: Color,
    val AchievementHeaderGradientBottom: Color,
    val DialogGradientTopRight: Color,
    val DialogGradientBottomLeft: Color,
    val ShadowOrange: Color,

    val MapPathOuter: Color,
    val MapPathInner: Color,
    val MapPathDash: Color,

    val DailyRewardInactiveNodeBg: Color,
    val DailyRewardInactiveNodeIcon: Color,
    val DailyRewardBadgeBg: Color,
    val DailyRewardBadgeText: Color,
    val DailyRewardActiveText: Color,
    val DailyRewardSubtitleText: Color,
    val DailyRewardInactiveLine: Color,
    val DailyRewardInactiveText: Color
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