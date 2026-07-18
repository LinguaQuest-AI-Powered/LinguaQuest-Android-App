package com.iti.linguaquest.core.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

object SharedBottomBarState {
    var heightPx by mutableIntStateOf(0)
}