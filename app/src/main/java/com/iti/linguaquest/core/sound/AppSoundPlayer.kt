package com.iti.linguaquest.core.sound

interface AppSoundPlayer {
    fun play(sound: AppSound, loop: Boolean = false)
    fun stop(sound: AppSound)
}