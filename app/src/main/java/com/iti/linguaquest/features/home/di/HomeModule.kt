package com.iti.linguaquest.features.home.di

import com.iti.linguaquest.features.home.data.dataSource.local.HomeLocalDataSource
import com.iti.linguaquest.features.home.data.dataSource.local.HomeLocalDataSourceImpl
import com.iti.linguaquest.features.home.data.dataSource.remote.DailyRewardApiService
import com.iti.linguaquest.features.home.data.dataSource.remote.DailyRewardRemoteDataSource
import com.iti.linguaquest.features.home.data.dataSource.remote.DailyRewardRemoteDataSourceImpl
import com.iti.linguaquest.features.home.data.dataSource.remote.HomeApiService
import com.iti.linguaquest.features.home.data.dataSource.remote.HomeRemoteDataSource
import com.iti.linguaquest.features.home.data.dataSource.remote.HomeRemoteDataSourceImpl
import com.iti.linguaquest.features.home.data.dataSource.remote.LanguagesApiService
import com.iti.linguaquest.features.home.data.dataSource.remote.LanguagesRemoteDataSource
import com.iti.linguaquest.features.home.data.dataSource.remote.LanguagesRemoteDataSourceImpl
import com.iti.linguaquest.features.home.data.repository.DailyRewardRepositoryImpl
import com.iti.linguaquest.features.home.data.repository.HomeRepositoryImpl
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
    abstract fun bindHomeLocalDataSource(
        homeLocalDataSourceImpl: HomeLocalDataSourceImpl
    ): HomeLocalDataSource


    @Binds
    @Singleton
    abstract fun bindHomeRemoteDataSource(
        homeRemoteDataSourceImpl: HomeRemoteDataSourceImpl
    ): HomeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDailyRewardRemoteDataSource(
        dailyRewardRemoteDataSourceImpl: DailyRewardRemoteDataSourceImpl
    ): DailyRewardRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLanguagesRemoteDataSource(
        languagesRemoteDataSourceImpl: LanguagesRemoteDataSourceImpl
    ): LanguagesRemoteDataSource


    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindDailyRewardRepository(
        dailyRewardRepositoryImpl: DailyRewardRepositoryImpl
    ): DailyRewardRepository

    @Binds
    @Singleton
    abstract fun bindLanguagesRepo(
        languagesRepoImpl: LanguagesRepoImpl
    ): LanguagesRepo


    companion object {
        @Provides
        @Singleton
        fun provideHomeApiService(retrofit: Retrofit): HomeApiService =
            retrofit.create(HomeApiService::class.java)

        @Provides
        @Singleton
        fun provideDailyRewardApiService(retrofit: Retrofit): DailyRewardApiService =
            retrofit.create(DailyRewardApiService::class.java)

        @Provides
        @Singleton
        fun provideLanguagesApiService(retrofit: Retrofit): LanguagesApiService =
            retrofit.create(LanguagesApiService::class.java)
    }
}
