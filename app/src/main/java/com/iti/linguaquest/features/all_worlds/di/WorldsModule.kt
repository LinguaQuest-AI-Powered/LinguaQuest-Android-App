package com.iti.linguaquest.features.all_worlds.di

import com.iti.linguaquest.features.all_worlds.data.remote.WorldsApiService
import com.iti.linguaquest.features.all_worlds.data.remote.WorldsRemoteDataSource
import com.iti.linguaquest.features.all_worlds.data.remote.WorldsRemoteDataSourceImpl
import com.iti.linguaquest.features.all_worlds.data.repository.WorldsRepositoryImpl
import com.iti.linguaquest.features.all_worlds.domain.repository.WorldsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorldsModule {

    @Binds
    @Singleton
    abstract fun bindWorldsRepository(
        impl: WorldsRepositoryImpl
    ): WorldsRepository

    @Binds
    @Singleton
    abstract fun bindWorldsRemoteDataSource(
        impl: WorldsRemoteDataSourceImpl
    ): WorldsRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideWorldsApiService(retrofit: Retrofit): WorldsApiService =
            retrofit.create(WorldsApiService::class.java)
    }
}
