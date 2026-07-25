package com.iti.linguaquest.features.game.di

import com.iti.linguaquest.features.game.data.remote.LevelApiService
import com.iti.linguaquest.features.game.data.remote.LevelRemoteDataSource
import com.iti.linguaquest.features.game.data.remote.LevelRemoteDataSourceImpl
import com.iti.linguaquest.features.game.data.repository.LevelRepositoryImpl
import com.iti.linguaquest.features.game.domain.repository.LevelRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LevelModule {

    @Binds
    @Singleton
    abstract fun bindLevelRepository(impl: LevelRepositoryImpl): LevelRepository

    @Binds
    @Singleton
    abstract fun bindLevelRemoteDataSource(impl: LevelRemoteDataSourceImpl): LevelRemoteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideLevelApiService(retrofit: Retrofit): LevelApiService =
            retrofit.create(LevelApiService::class.java)
    }
}
