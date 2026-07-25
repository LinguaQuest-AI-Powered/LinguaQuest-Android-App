package com.iti.linguaquest.features.auth.di

import com.iti.linguaquest.features.auth.data.datasource.remote.AuthRemoteDataSource
import com.iti.linguaquest.features.auth.data.datasource.remote.AuthRemoteDataSourceImpl
import com.iti.linguaquest.features.auth.data.repository.AuthRepositoryImpl
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource
}