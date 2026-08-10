package com.iti.linguaquest.core.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.iti.linguaquest.core.database.home.HomeDao
import com.iti.linguaquest.core.database.home.HomeEntity
import com.iti.linguaquest.core.database.notification.NotificationDao
import com.iti.linguaquest.core.database.notification.NotificationEntity
import com.iti.linguaquest.core.database.profile.ProfileDao
import com.iti.linguaquest.core.database.profile.ProfileEntity
import com.iti.linguaquest.core.database.lockscreen.LockScreenWordDao
import com.iti.linguaquest.core.database.lockscreen.LockScreenWordEntity
import com.iti.linguaquest.core.database.word.WordDao
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.database.languages.LanguagesDao
import com.iti.linguaquest.core.database.languages.UserLanguageEntity

@Database(
    entities = [
        WordEntity::class,
        ProfileEntity::class,
        LockScreenWordEntity::class,
        HomeEntity::class,
        NotificationEntity::class,
        UserLanguageEntity::class
    ],
    version = 12,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun profileDao(): ProfileDao
    abstract fun lockScreenWordDao(): LockScreenWordDao
    abstract fun homeDao(): HomeDao
    abstract fun notificationDao(): NotificationDao
    abstract fun languagesDao(): LanguagesDao
}