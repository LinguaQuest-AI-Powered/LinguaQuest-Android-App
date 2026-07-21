package com.iti.linguaquest.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.popToLogin() {
    if (contains(RootScreen.Login)) {
        while (lastOrNull() != RootScreen.Login) {
            removeLastOrNull()
        }
    } else {
        clear()
        add(RootScreen.Login)
    }
}
