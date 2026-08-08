package com.iti.linguaquest.core.language.di

import com.iti.linguaquest.core.language.data.manager.LanguageManagerImpl
import com.iti.linguaquest.core.language.domain.manager.LanguageManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreLanguageModule {

    @Binds
    @Singleton
    abstract fun bindLanguageManager(
        impl: LanguageManagerImpl
    ): LanguageManager
}

