package com.iti.linguaquest.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.popToLogin() {
    if (any { it is RootScreen.Login }) {
        while (lastOrNull() !is RootScreen.Login) {
            removeLastOrNull()
        }
    } else {
        clear()
        add(RootScreen.Login())
    }
}
