package com.iti.linguaquest.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavScreen(
    val routeName: String,
    val icon: ImageVector,
    val route: Screen
) {
    Home("Home", Icons.Default.Home, Screen.Home),
    Profile("Profile", Icons.Default.Person, Screen.Profile)
}

sealed interface Screen {
    data object Home : Screen
    data object Profile : Screen
    data class Details(val id: Int) : Screen
}
