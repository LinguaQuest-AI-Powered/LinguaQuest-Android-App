package com.iti.linguaquest.core.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SharedBackgroundState {
    var showBackground by mutableStateOf(false)
    var showDarkEffect by mutableStateOf(false)
}
