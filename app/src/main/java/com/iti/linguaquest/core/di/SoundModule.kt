package com.iti.linguaquest.core.di

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.sound.AppSoundPlayer
import com.iti.linguaquest.core.sound.SoundManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SoundModule {

    @Provides
    @Singleton
    fun provideSoundPool(): SoundPool {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        return SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(attributes)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppSoundPlayer(
        @ApplicationContext context: Context,
        soundPool: SoundPool,
        userPreferences: UserPreferencesRepository,
        applicationScope: CoroutineScope
    ): AppSoundPlayer {
        return SoundManager(context, soundPool, userPreferences, applicationScope)
    }
}