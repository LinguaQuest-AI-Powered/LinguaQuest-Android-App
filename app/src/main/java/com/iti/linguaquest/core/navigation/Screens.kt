package com.iti.linguaquest.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

enum class BottomNavScreen(
    val routeName: String,
    val icon: ImageVector,
    val route: NestedScreen
) {
    Home("Home", Icons.Default.Home, NestedScreen.Home),
    Profile("Profile", Icons.Default.Person, NestedScreen.Profile)
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
    data object Main : RootScreen

    @Serializable
    data class Details(val id: Int) : RootScreen
}

@Serializable
sealed interface NestedScreen : NavKey {
    @Serializable
    data object Home : NestedScreen
    @Serializable
    data object Profile : NestedScreen
}
