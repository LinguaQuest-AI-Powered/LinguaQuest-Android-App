package com.iti.linguaquest.core.sound

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSoundPlayer = staticCompositionLocalOf<AppSoundPlayer> {
    object : AppSoundPlayer {
        override fun play(sound: AppSound) {}
    }
}