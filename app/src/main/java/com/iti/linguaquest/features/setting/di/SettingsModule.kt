package com.iti.linguaquest.features.setting.di

import com.iti.linguaquest.features.setting.data.manager.LanguageManagerImpl
import com.iti.linguaquest.features.setting.domain.manager.LanguageManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindLanguageManager(
        impl: LanguageManagerImpl
    ): LanguageManager
}
