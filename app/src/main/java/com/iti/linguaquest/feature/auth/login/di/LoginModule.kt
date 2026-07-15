package com.iti.linguaquest.feature.auth.login.di

import com.iti.linguaquest.feature.auth.login.data.repository.LoginRepositoryImpl
import com.iti.linguaquest.feature.auth.login.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    @Binds
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl,
    ): LoginRepository
}
