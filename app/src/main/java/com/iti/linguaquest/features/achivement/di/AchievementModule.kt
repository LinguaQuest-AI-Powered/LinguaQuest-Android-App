package com.iti.linguaquest.features.achivement.di

import com.iti.linguaquest.features.achivement.data.datasource.remote.AchievementApiService
import com.iti.linguaquest.features.achivement.data.datasource.remote.AchievementRemoteDataSource
import com.iti.linguaquest.features.achivement.data.datasource.remote.AchievementRemoteDataSourceImpl
import com.iti.linguaquest.features.achivement.data.repository.AchievementRepositoryImpl
import com.iti.linguaquest.features.achivement.domain.repository.AchievementRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AchievementModule {

    @Binds
    @Singleton
    abstract fun bindAchievementRemoteDataSource(
        achievementRemoteDataSourceImpl: AchievementRemoteDataSourceImpl
    ): AchievementRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAchievementRepository(
        achievementRepositoryImpl: AchievementRepositoryImpl
    ): AchievementRepository

    companion object {
        @Provides
        @Singleton
        fun provideAchievementApiService(retrofit: Retrofit): AchievementApiService =
            retrofit.create(AchievementApiService::class.java)
    }
}
