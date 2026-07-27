package com.iti.linguaquest.core.wallet.di

import com.iti.linguaquest.core.wallet.data.datasource.local.WalletLocalDataSource
import com.iti.linguaquest.core.wallet.data.datasource.local.WalletLocalDataSourceImpl
import com.iti.linguaquest.core.wallet.data.datasource.remote.WalletApiService
import com.iti.linguaquest.core.wallet.data.datasource.remote.WalletRemoteDataSource
import com.iti.linguaquest.core.wallet.data.datasource.remote.WalletRemoteDataSourceImpl
import com.iti.linguaquest.core.wallet.data.repository.WalletRepositoryImpl
import com.iti.linguaquest.core.wallet.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WalletModule {
    
    @Provides
    @Singleton
    fun provideWalletApiService(retrofit: Retrofit): WalletApiService {
        return retrofit.create(WalletApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WalletRepositoryBindings {

    @Binds
    @Singleton
    abstract fun bindWalletLocalDataSource(
        impl: WalletLocalDataSourceImpl
    ): WalletLocalDataSource

    @Binds
    @Singleton
    abstract fun bindWalletRemoteDataSource(
        impl: WalletRemoteDataSourceImpl
    ): WalletRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        impl: WalletRepositoryImpl
    ): WalletRepository
}
