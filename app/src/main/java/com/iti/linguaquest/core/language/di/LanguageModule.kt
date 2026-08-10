package com.iti.linguaquest.core.language.di

import com.iti.linguaquest.core.language.data.datasource.local.SupportedLanguagesCacheDataSource
import com.iti.linguaquest.core.language.data.datasource.local.SupportedLanguagesCacheDataSourceImpl
import com.iti.linguaquest.core.language.data.datasource.remote.LanguageRemoteDataSource
import com.iti.linguaquest.core.language.data.datasource.remote.LanguageRemoteDataSourceImpl
import com.iti.linguaquest.core.language.data.repository.SupportedLanguagesRepositoryImpl
import com.iti.linguaquest.core.language.domain.repository.SupportedLanguagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LanguageModule {

    @Binds
    @Singleton
    abstract fun bindSupportedLanguagesCacheDataSource(
        impl: SupportedLanguagesCacheDataSourceImpl
    ): SupportedLanguagesCacheDataSource

    @Binds
    @Singleton
    abstract fun bindLanguageRemoteDataSource(
        impl: LanguageRemoteDataSourceImpl
    ): LanguageRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindSupportedLanguagesRepository(
        impl: SupportedLanguagesRepositoryImpl
    ): SupportedLanguagesRepository
}
