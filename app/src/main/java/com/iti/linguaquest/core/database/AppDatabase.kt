package com.iti.linguaquest.core.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.iti.linguaquest.core.database.profile.ProfileDao
import com.iti.linguaquest.core.database.profile.ProfileEntity
import com.iti.linguaquest.core.database.word.WordDao
import com.iti.linguaquest.core.database.word.WordEntity

@Database(
    entities = [WordEntity::class, ProfileEntity::class],
    version = 6  ,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun profileDao(): ProfileDao
}