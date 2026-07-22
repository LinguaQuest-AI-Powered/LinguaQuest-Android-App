package com.iti.linguaquest.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

import com.iti.linguaquest.R

enum class BottomNavScreen(
    @androidx.annotation.StringRes val labelRes: Int,
    val icon: ImageVector,
    val route: NestedScreen
) {
    Home(R.string.home_label, Icons.Default.Home, NestedScreen.Home),
    Gallery(R.string.gallery_label, Icons.Default.PhotoLibrary, NestedScreen.Gallery),
    Profile(R.string.profile_label, Icons.Default.Person, NestedScreen.Profile)
}

@Serializable
sealed interface RootScreen : NavKey {

    @Serializable
    data object Splash : RootScreen

    @Serializable
    data object Onboarding : RootScreen

    @Serializable
    data object Languages : RootScreen

    @Serializable
    data object OnboardingLevel : RootScreen

    // REMOVED: data class Level(val worldId: Int, val levelNumber: Int) : RootScreen

    @Serializable
    data object Login : RootScreen
    @Serializable
    data object SignUp : RootScreen
    @Serializable
    data object ForgotPassword : RootScreen
    @Serializable
    data class NewPassword(val resetToken: String) : RootScreen
    @Serializable
    data object Main : RootScreen

    @Serializable
    data class GameFlow(val worldId: Int, val levelNumber: Int) : RootScreen // UPDATED

    @Serializable
    data class Map(val worldId: Int) : RootScreen

    @Serializable
    data class Details(val id: Int) : RootScreen

    @Serializable
    data class Review(val wordId: Int) : RootScreen
    @Serializable
    data class OTP(val email: String, val isPasswordReset: Boolean) : RootScreen

    @Serializable
    data object Settings : RootScreen
    @Serializable
    data object LockScreenVocabulary : RootScreen
    @Serializable
    data class LockScreenWordDetail(val wordId: Int) : RootScreen

    @Serializable
    data object Leaderboard : RootScreen
    @Serializable
    data object AllWorlds : RootScreen
    @Serializable
    data object EditProfile : RootScreen
    @Serializable
    data object AddLanguages : RootScreen
    @Serializable
    data object Achievement : RootScreen
}
@Serializable
sealed interface NestedScreen : NavKey {
    @Serializable
    data object Gallery : NestedScreen
    @Serializable
    data object Home : NestedScreen
    @Serializable
    data object Profile : NestedScreen
}


@Serializable
sealed interface GameFlowScreen : NavKey {
    @Serializable
    data object Level : GameFlowScreen

    @Serializable
    data object Camera : GameFlowScreen

    @Serializable
    data object Result : GameFlowScreen
    @Serializable
    object Processing : GameFlowScreen
}
