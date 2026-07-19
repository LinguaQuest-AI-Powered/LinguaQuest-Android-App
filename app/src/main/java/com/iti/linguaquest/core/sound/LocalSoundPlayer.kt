package com.iti.linguaquest.core.sound

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSoundPlayer = staticCompositionLocalOf<AppSoundPlayer> {
    error("No AppSoundPlayer provided!")
}