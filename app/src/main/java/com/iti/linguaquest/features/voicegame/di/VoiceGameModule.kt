package com.iti.linguaquest.features.voicegame.di

import com.iti.linguaquest.features.voicegame.data.repository.VoiceGameRepositoryImpl
import com.iti.linguaquest.features.voicegame.domain.repository.VoiceGameRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VoiceGameModule {

    @Binds
    @Singleton
    abstract fun bindVoiceGameRepository(
        impl: VoiceGameRepositoryImpl
    ): VoiceGameRepository
}
