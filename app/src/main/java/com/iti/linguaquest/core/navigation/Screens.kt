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
    data object Level : RootScreen

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
    data class Details(val id: Int) : RootScreen

    @Serializable
    data class Review(val wordId: Int) : RootScreen
    @Serializable
    data class OTP(val email: String, val isPasswordReset: Boolean) : RootScreen
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
