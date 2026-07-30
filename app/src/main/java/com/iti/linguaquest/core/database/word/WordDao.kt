package com.iti.linguaquest.core.database.word

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Update
    suspend fun updateWord(word: WordEntity)

    @Delete
    suspend fun deleteWord(word: WordEntity)

    @Query("DELETE FROM words WHERE id = :wordId")
    suspend fun deleteWordById(wordId: Int)

    @Query("DELETE FROM words")
    suspend fun clearWords()

    @Transaction
    suspend fun replaceAllWords(words: List<WordEntity>) {
        clearWords()
        if (words.isNotEmpty()) {
            insertWords(words)
        }
    }

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE id = :wordId")
    suspend fun getWordById(wordId: Int): WordEntity?

     @Query("SELECT * FROM words WHERE isCorrect = 1")
    fun getIsCorrectWords(): Flow<List<WordEntity>>

    @Query("UPDATE words SET isCorrect = :isCorrect WHERE id = :wordId")
    suspend fun setCorrectStatus(wordId: Int, isCorrect: Boolean)

     @Query("SELECT * FROM words ORDER BY id DESC")
    fun getWordsWithImages(): Flow<List<WordEntity>>
}
