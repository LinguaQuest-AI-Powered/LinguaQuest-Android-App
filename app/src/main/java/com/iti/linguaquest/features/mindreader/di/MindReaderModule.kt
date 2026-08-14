package com.iti.linguaquest.features.mindreader.di

import com.iti.linguaquest.features.mindreader.data.datasource.local.MindReaderLocalDataSource
import com.iti.linguaquest.features.mindreader.data.datasource.local.MindReaderLocalDataSourceImpl
import com.iti.linguaquest.features.mindreader.data.datasource.remote.GeminiMindReaderService
import com.iti.linguaquest.features.mindreader.data.datasource.remote.MindReaderRemoteDataSource
import com.iti.linguaquest.features.mindreader.data.repository.MindReaderRepositoryImpl
import com.iti.linguaquest.features.mindreader.domain.repository.MindReaderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MindReaderModule {

    @Binds
    @Singleton
    abstract fun bindMindReaderLocalDataSource(
        impl: MindReaderLocalDataSourceImpl
    ): MindReaderLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMindReaderRemoteDataSource(
        impl: GeminiMindReaderService
    ): MindReaderRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMindReaderRepository(
        impl: MindReaderRepositoryImpl
    ): MindReaderRepository
}
