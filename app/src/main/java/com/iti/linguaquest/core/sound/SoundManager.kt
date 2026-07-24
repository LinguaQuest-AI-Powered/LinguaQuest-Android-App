package com.iti.linguaquest.core.sound

import android.content.Context
import android.media.SoundPool
import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val soundPool: SoundPool,
    private val userPreferences: UserPreferencesRepository,
    applicationScope: CoroutineScope
) : AppSoundPlayer {

    private val soundMap = mutableMapOf<AppSound, Int>()
    private var isSoundEnabled = true

    init {
        AppSound.entries.forEach { sound ->
            soundMap[sound] = soundPool.load(context, sound.resId, 1)
        }

        applicationScope.launch {
            userPreferences.soundEnabled.collect { enabled ->
                isSoundEnabled = enabled
            }
        }
    }

    override fun play(sound: AppSound) {

        if(!isSoundEnabled) return

        val soundId = soundMap[sound] ?: return
        soundPool.play(
            soundId,
            sound.volume,
            sound.volume,
            1,
            0,
            1f
        )
    }
}