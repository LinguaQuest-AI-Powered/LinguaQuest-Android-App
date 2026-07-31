package com.iti.linguaquest.features.mindreader.di

import com.iti.linguaquest.features.mindreader.data.datasource.MindReaderDataSource
import com.iti.linguaquest.features.mindreader.data.datasource.MindReaderJsonDataSourceImpl
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
    abstract fun bindMindReaderDataSource(
        impl: MindReaderJsonDataSourceImpl
    ): MindReaderDataSource

    @Binds
    @Singleton
    abstract fun bindMindReaderRepository(
        impl: MindReaderRepositoryImpl
    ): MindReaderRepository
}
