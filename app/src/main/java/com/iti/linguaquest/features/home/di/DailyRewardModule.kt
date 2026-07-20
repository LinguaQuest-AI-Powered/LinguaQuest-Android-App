package com.iti.linguaquest.features.home.di


import com.iti.linguaquest.features.home.data.fake.FakeDailyRewardRepository
import com.iti.linguaquest.features.home.data.remote.DailyRewardApiService
import com.iti.linguaquest.features.home.domain.repository.DailyRewardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DailyRewardModule {

    @Binds
    @Singleton
    abstract fun bindDailyRewardRepository(impl: FakeDailyRewardRepository): DailyRewardRepository
    // Swap to DailyRewardRepositoryImpl(remoteDataSource) when backend is ready

    companion object {
        @Provides
        @Singleton
        fun provideDailyRewardApiService(retrofit: Retrofit): DailyRewardApiService =
            retrofit.create(DailyRewardApiService::class.java)
    }
}