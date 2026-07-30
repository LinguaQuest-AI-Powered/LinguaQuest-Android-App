package com.iti.linguaquest.features.lockscreen.di

import com.iti.linguaquest.features.lockscreen.data.local.LockScreenLocalDataSource
import com.iti.linguaquest.features.lockscreen.data.local.LockScreenLocalDataSourceImpl
import com.iti.linguaquest.features.lockscreen.data.local.LockScreenPreferencesLocalDataSource
import com.iti.linguaquest.features.lockscreen.data.local.LockScreenPreferencesLocalDataSourceImpl
import com.iti.linguaquest.features.lockscreen.data.remote.CoinsApiService
import com.iti.linguaquest.features.lockscreen.data.remote.LockScreenRemoteDataSource
import com.iti.linguaquest.features.lockscreen.data.remote.LockScreenRemoteDataSourceImpl
import com.iti.linguaquest.features.lockscreen.data.remote.PromptBuilder
import com.iti.linguaquest.features.lockscreen.data.repository.LockScreenRepositoryImpl
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LockScreenBindings {

    @Binds
    @Singleton
    abstract fun bindLockScreenPreferencesLocalDataSource(
        impl: LockScreenPreferencesLocalDataSourceImpl
    ): LockScreenPreferencesLocalDataSource

    @Binds
    @Singleton
    abstract fun bindLockScreenLocalDataSource(
        impl: LockScreenLocalDataSourceImpl
    ): LockScreenLocalDataSource

    @Binds
    @Singleton
    abstract fun bindLockScreenRemoteDataSource(
        impl: LockScreenRemoteDataSourceImpl
    ): LockScreenRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLockScreenRepository(
        impl: LockScreenRepositoryImpl
    ): LockScreenRepository
}

@Module
@InstallIn(SingletonComponent::class)
object LockScreenProvides {

    @Provides
    @Singleton
    fun providePromptBuilder(): PromptBuilder = PromptBuilder()

    @Provides
    @Singleton
    fun provideCoinsApiService(retrofit: Retrofit): CoinsApiService =
        retrofit.create(CoinsApiService::class.java)
}
