package com.iti.linguaquest.core.navigation

sealed interface Screen {
    data object Home : Screen
    data class Details(val id: Int) : Screen
}
