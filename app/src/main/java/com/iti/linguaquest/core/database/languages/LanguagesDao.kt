package com.iti.linguaquest.core.database.languages

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguagesDao {
    @Query("SELECT * FROM user_languages")
    fun getMyLanguagesFlow(): Flow<List<UserLanguageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyLanguages(languages: List<UserLanguageEntity>)

    @Query("DELETE FROM user_languages")
    suspend fun clearMyLanguages()

    @Transaction
    suspend fun clearAndInsertMyLanguages(languages: List<UserLanguageEntity>) {
        clearMyLanguages()
        insertMyLanguages(languages)
    }
}
