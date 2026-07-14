package com.iti.linguaquest.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.iti.linguaquest.core.preferences.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.preferences.UserPreferencesLocalDataSourceImpl
import com.iti.linguaquest.core.preferences.UserPreferencesRepository
import com.iti.linguaquest.core.preferences.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
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
}