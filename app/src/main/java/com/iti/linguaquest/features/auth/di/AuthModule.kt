package com.iti.linguaquest.features.auth.di

import com.iti.linguaquest.features.auth.data.repository.AuthRepositoryImpl
import com.iti.linguaquest.features.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindLoginRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository
}
