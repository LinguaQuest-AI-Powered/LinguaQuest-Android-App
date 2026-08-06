package com.iti.linguaquest.core.di

import android.content.Context
import androidx.room3.Room
import com.iti.linguaquest.core.database.AppDatabase
import com.iti.linguaquest.core.database.home.HomeDao
import com.iti.linguaquest.core.database.profile.ProfileDao
import com.iti.linguaquest.core.database.lockscreen.LockScreenWordDao
import com.iti.linguaquest.core.database.notification.NotificationDao
import com.iti.linguaquest.core.database.word.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideWordDao(appDatabase: AppDatabase): WordDao {
        return appDatabase.wordDao()
    }

    @Provides
    @Singleton
    fun provideProfileDao(appDatabase: AppDatabase): ProfileDao {
        return appDatabase.profileDao()
    }

    @Provides
    @Singleton
    fun provideLockScreenWordDao(appDatabase: AppDatabase): LockScreenWordDao {
        return appDatabase.lockScreenWordDao()
    }

    @Provides
    @Singleton
    fun provideHomeDao(appDatabase: AppDatabase): HomeDao {
        return appDatabase.homeDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(appDatabase: AppDatabase): NotificationDao {
        return appDatabase.notificationDao()
    }
}

