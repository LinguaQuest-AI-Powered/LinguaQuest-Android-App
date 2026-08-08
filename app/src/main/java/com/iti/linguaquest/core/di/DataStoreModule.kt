package com.iti.linguaquest.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSource
import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSourceImpl
import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSourceImpl
import com.iti.linguaquest.core.cache.data.repository.SessionManagerRepositoryImpl
import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.cache.data.repository.UserPreferencesRepositoryImpl
import com.iti.linguaquest.core.cache.domain.repository.SessionManagerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserSettingsDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SessionDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WalletDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthCacheDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")
val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session_manager")
val Context.walletDataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet")
val Context.authCacheDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_cache_prefs")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @UserSettingsDataStore
    @Provides
    @Singleton
    fun provideUserSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @SessionDataStore
    @Provides
    @Singleton
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.sessionDataStore
    }

    @WalletDataStore
    @Provides
    @Singleton
    fun provideWalletDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.walletDataStore
    }

    @AuthCacheDataStore
    @Provides
    @Singleton
    fun provideAuthCacheDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.authCacheDataStore
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindings {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesLocalDataSource(
        impl: UserPreferencesLocalDataSourceImpl
    ): UserPreferencesLocalDataSource

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        impl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindSessionManagerDataSource(
        impl: SessionManagerDataSourceImpl
    ): SessionManagerDataSource

    @Binds
    @Singleton
    abstract fun bindSessionManagerRepository(
        impl: SessionManagerRepositoryImpl
    ): SessionManagerRepository
}