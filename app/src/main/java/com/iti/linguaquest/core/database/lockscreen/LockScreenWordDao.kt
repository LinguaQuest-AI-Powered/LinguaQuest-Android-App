package com.iti.linguaquest.core.database.lockscreen

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LockScreenWordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(words: List<LockScreenWordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: LockScreenWordEntity)

    @Query("SELECT * FROM lock_screen_words WHERE status = 'PENDING' ORDER BY createdAt ASC LIMIT 1")
    fun getPendingWord(): Flow<LockScreenWordEntity?>

    @Query("SELECT * FROM lock_screen_words WHERE status = 'PENDING' ORDER BY createdAt ASC LIMIT 1")
    suspend fun getPendingWordOnce(): LockScreenWordEntity?

    @Query("SELECT * FROM lock_screen_words WHERE status = 'PENDING' ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomPendingWordOnce(): LockScreenWordEntity?

    @Query("SELECT COUNT(*) FROM lock_screen_words WHERE status = 'PENDING'")
    fun pendingCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM lock_screen_words WHERE status = 'PENDING'")
    suspend fun pendingCountOnce(): Int

    @Query("SELECT * FROM lock_screen_words ORDER BY createdAt DESC")
    fun allWords(): Flow<List<LockScreenWordEntity>>

    @Query("SELECT * FROM lock_screen_words ORDER BY createdAt DESC")
    suspend fun allWordsOnce(): List<LockScreenWordEntity>

    @Query("SELECT * FROM lock_screen_words WHERE id = :wordId LIMIT 1")
    suspend fun getById(wordId: Int): LockScreenWordEntity?

    @Query("SELECT * FROM lock_screen_words WHERE id = :wordId LIMIT 1")
    fun observeById(wordId: Int): Flow<LockScreenWordEntity?>

    @Query("SELECT word FROM lock_screen_words ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentWords(limit: Int): List<String>

    @Query("SELECT * FROM lock_screen_words WHERE status IN ('POSTED', 'OPENED') ORDER BY postedAt DESC")
    fun getPostedOrOpenedWords(): Flow<List<LockScreenWordEntity>>

    @Query("SELECT * FROM lock_screen_words WHERE status IN ('POSTED', 'OPENED') ORDER BY postedAt DESC")
    suspend fun getPostedOrOpenedWordsOnce(): List<LockScreenWordEntity>

    @Query(
        "UPDATE lock_screen_words SET status = :status, postedAt = COALESCE(:postedAt, postedAt), openedAt = COALESCE(:openedAt, openedAt) WHERE id = :wordId"
    )
    suspend fun updateStatus(
        wordId: Int,
        status: String,
        postedAt: Long? = null,
        openedAt: Long? = null
    )

    @Query("DELETE FROM lock_screen_words")
    suspend fun clearAll()

    @Query("DELETE FROM lock_screen_words WHERE targetLanguage = :targetLanguage")
    suspend fun clearByTargetLanguage(targetLanguage: String)

    @Query(
        "DELETE FROM lock_screen_words WHERE targetLanguage = :targetLanguage AND proficiencyLevel = :proficiencyLevel"
    )
    suspend fun clearByTargetLanguageAndLevel(
        targetLanguage: String,
        proficiencyLevel: String
    )
}
