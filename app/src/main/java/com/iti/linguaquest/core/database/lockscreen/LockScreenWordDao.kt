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

    @Query("SELECT * FROM lock_screen_words WHERE userId = :userId AND status = 'PENDING' AND targetLanguage = :targetLanguage ORDER BY createdAt ASC LIMIT 1")
    fun getPendingWord(userId: Int, targetLanguage: String): Flow<LockScreenWordEntity?>

    @Query("SELECT * FROM lock_screen_words WHERE userId = :userId AND status = 'PENDING' AND targetLanguage = :targetLanguage ORDER BY createdAt ASC LIMIT 1")
    suspend fun getPendingWordOnce(userId: Int, targetLanguage: String): LockScreenWordEntity?

    @Query("SELECT * FROM lock_screen_words WHERE userId = :userId AND status = 'PENDING' AND targetLanguage = :targetLanguage ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomPendingWordOnce(userId: Int, targetLanguage: String): LockScreenWordEntity?

    @Query("SELECT COUNT(*) FROM lock_screen_words WHERE userId = :userId AND status = 'PENDING' AND targetLanguage = :targetLanguage")
    fun pendingCount(userId: Int, targetLanguage: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM lock_screen_words WHERE userId = :userId AND status = 'PENDING' AND targetLanguage = :targetLanguage")
    suspend fun pendingCountOnce(userId: Int, targetLanguage: String): Int

    @Query("SELECT * FROM lock_screen_words WHERE userId = :userId ORDER BY createdAt DESC")
    fun allWords(userId: Int): Flow<List<LockScreenWordEntity>>

    @Query("SELECT * FROM lock_screen_words WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun allWordsOnce(userId: Int): List<LockScreenWordEntity>

    @Query("SELECT * FROM lock_screen_words WHERE id = :wordId LIMIT 1")
    suspend fun getById(wordId: Int): LockScreenWordEntity?

    @Query("SELECT * FROM lock_screen_words WHERE id = :wordId LIMIT 1")
    fun observeById(wordId: Int): Flow<LockScreenWordEntity?>

    @Query("SELECT word FROM lock_screen_words WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentWords(userId: Int, limit: Int): List<String>

    @Query("SELECT * FROM lock_screen_words WHERE userId = :userId AND status IN ('POSTED', 'OPENED') ORDER BY postedAt DESC")
    fun getPostedOrOpenedWords(userId: Int): Flow<List<LockScreenWordEntity>>

    @Query("SELECT * FROM lock_screen_words WHERE userId = :userId AND status IN ('POSTED', 'OPENED') ORDER BY postedAt DESC")
    suspend fun getPostedOrOpenedWordsOnce(userId: Int): List<LockScreenWordEntity>

    @Query(
        "UPDATE lock_screen_words SET status = :status, postedAt = COALESCE(:postedAt, postedAt), openedAt = COALESCE(:openedAt, openedAt) WHERE id = :wordId"
    )
    suspend fun updateStatus(
        wordId: Int,
        status: String,
        postedAt: Long? = null,
        openedAt: Long? = null
    )

    @Query("DELETE FROM lock_screen_words WHERE userId = :userId")
    suspend fun clearAll(userId: Int)

    @Query("DELETE FROM lock_screen_words WHERE userId = :userId AND targetLanguage = :targetLanguage")
    suspend fun clearByTargetLanguage(userId: Int, targetLanguage: String)

    @Query(
        "DELETE FROM lock_screen_words WHERE userId = :userId AND targetLanguage = :targetLanguage AND proficiencyLevel = :proficiencyLevel"
    )
    suspend fun clearByTargetLanguageAndLevel(
        userId: Int,
        targetLanguage: String,
        proficiencyLevel: String
    )
}
