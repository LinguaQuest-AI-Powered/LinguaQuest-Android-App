package com.iti.linguaquest.core.database.word

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
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

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE id = :wordId")
    suspend fun getWordById(wordId: Int): WordEntity?

     @Query("SELECT * FROM words WHERE isFavorite = 1")
    fun getFavoriteWords(): Flow<List<WordEntity>>

    @Query("UPDATE words SET isFavorite = :isFavorite WHERE id = :wordId")
    suspend fun setFavoriteStatus(wordId: Int, isFavorite: Boolean)

     @Query("SELECT * FROM words WHERE imagePath != ''")
    fun getWordsWithImages(): Flow<List<WordEntity>>
}