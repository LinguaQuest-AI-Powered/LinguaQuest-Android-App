package com.iti.linguaquest.core.di

import android.content.Context
import androidx.room3.Room
import com.iti.linguaquest.core.database.AppDatabase
import com.iti.linguaquest.core.database.word.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordDao(appDatabase: AppDatabase): WordDao {
        return appDatabase.wordDao()
    }
}