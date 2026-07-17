package com.iti.linguaquest.core.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.iti.linguaquest.core.database.word.WordDao
import com.iti.linguaquest.core.database.word.WordEntity

@Database(
    entities = [WordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}