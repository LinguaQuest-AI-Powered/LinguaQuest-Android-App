package com.iti.linguaquest.features.gallery.di

import com.iti.linguaquest.features.gallery.data.datasource.WordLocalDataSource
import com.iti.linguaquest.features.gallery.data.datasource.WordLocalDataSourceImpl
import com.iti.linguaquest.features.gallery.data.repository.WordRepositoryImpl
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWordLocalDataSource(
        wordLocalDataSourceImpl: WordLocalDataSourceImpl
    ): WordLocalDataSource

    @Binds
    @Singleton
    abstract fun bindWordRepository(
        wordRepositoryImpl: WordRepositoryImpl
    ): WordRepository
}