package com.iti.linguaquest.features.leaderboard.di


import com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource.LeaderboardApiService
import com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource.LeaderboardRemoteDataSource
import com.iti.linguaquest.features.leaderboard.data.datasource.remotedatesource.LeaderboardRemoteDataSourceImpl
import com.iti.linguaquest.features.leaderboard.data.datasource.repository.LeaderboardRepositoryImpl
import com.iti.linguaquest.features.leaderboard.domain.repository.LeaderboardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class LeaderboardModule {

    @Binds
    @Singleton
    abstract fun bindLeaderboardRemoteDataSource(
        leaderboardRemoteDataSourceImpl: LeaderboardRemoteDataSourceImpl
    ): LeaderboardRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLeaderboardRepository(
        leaderboardRepositoryImpl: LeaderboardRepositoryImpl
    ): LeaderboardRepository

    companion object {
        @Provides
        @Singleton
        fun provideLeaderboardApiService(retrofit: Retrofit): LeaderboardApiService =
            retrofit.create(LeaderboardApiService::class.java)
    }
}