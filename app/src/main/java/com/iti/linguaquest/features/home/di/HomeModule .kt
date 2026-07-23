package com.iti.linguaquest.features.home.di

import com.iti.linguaquest.features.home.data.fake.FakeDailyRewardRepository
import com.iti.linguaquest.features.home.data.fake.FakeHomeRepository
import com.iti.linguaquest.features.home.data.remote.DailyRewardApiService
import com.iti.linguaquest.features.home.data.remote.HomeApiService
import com.iti.linguaquest.features.home.data.remote.HomeRemoteDataSource
import com.iti.linguaquest.features.home.data.remote.HomeRemoteDataSourceImpl
import com.iti.linguaquest.features.home.data.remote.LanguagesApiService
import com.iti.linguaquest.features.home.data.remote.LanguagesRemoteDataSource
import com.iti.linguaquest.features.home.data.remote.LanguagesRemoteDataSourceImpl
import com.iti.linguaquest.features.home.data.repository.LanguagesRepoImpl
import com.iti.linguaquest.features.home.domain.repository.DailyRewardRepository
import com.iti.linguaquest.features.home.domain.repository.HomeRepository
import com.iti.linguaquest.features.home.domain.repository.LanguagesRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: FakeHomeRepository): HomeRepository

    @Binds
    @Singleton
    abstract fun bindLanguagesRepo(impl: LanguagesRepoImpl): LanguagesRepo

    @Binds
    @Singleton
    abstract fun bindHomeRemoteDataSource(impl: HomeRemoteDataSourceImpl): HomeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLanguagesRemoteDataSource(impl: LanguagesRemoteDataSourceImpl): LanguagesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDailyRewardRepository(impl: FakeDailyRewardRepository): DailyRewardRepository
    // Swap to DailyRewardRepositoryImpl(remoteDataSource) when backend is ready

    companion object {
        @Provides
        @Singleton
        fun provideHomeApiService(retrofit: Retrofit): HomeApiService =
            retrofit.create(HomeApiService::class.java)

        @Provides
        @Singleton
        fun provideLanguagesApiService(retrofit: Retrofit): LanguagesApiService =
            retrofit.create(LanguagesApiService::class.java)

        @Provides
        @Singleton
        fun provideDailyRewardApiService(retrofit: Retrofit): DailyRewardApiService =
            retrofit.create(DailyRewardApiService::class.java)
    }
}