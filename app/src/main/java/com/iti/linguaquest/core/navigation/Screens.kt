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
    data object Main : RootScreen
    @Serializable
    data class Details(val id: Int) : RootScreen
    @Serializable
    data object OTP : RootScreen
}

@Serializable
sealed interface NestedScreen : NavKey {
    @Serializable
    data object Home : NestedScreen
    @Serializable
    data object Profile : NestedScreen
}
