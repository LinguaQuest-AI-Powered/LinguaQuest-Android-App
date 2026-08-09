package com.iti.linguaquest.features.dailymission.di

import com.iti.linguaquest.features.dailymission.data.remote.DailyMissionApiService
import com.iti.linguaquest.features.dailymission.data.remote.DailyMissionRemoteDataSource
import com.iti.linguaquest.features.dailymission.data.remote.DailyMissionRemoteDataSourceImpl
import com.iti.linguaquest.features.dailymission.data.repository.DailyMissionRepositoryImpl
import com.iti.linguaquest.features.dailymission.domain.repository.DailyMissionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DailyMissionNetworkModule {

    @Provides
    @Singleton
    fun provideDailyMissionApiService(retrofit: Retrofit): DailyMissionApiService {
        return retrofit.create(DailyMissionApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DailyMissionRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDailyMissionRemoteDataSource(
        impl: DailyMissionRemoteDataSourceImpl
    ): DailyMissionRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDailyMissionRepository(
        impl: DailyMissionRepositoryImpl
    ): DailyMissionRepository
}
