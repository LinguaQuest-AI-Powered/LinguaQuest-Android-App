package com.iti.linguaquest.features.gallery.data.datasource

import com.iti.linguaquest.core.database.safeDatabaseCall
import com.iti.linguaquest.core.database.word.WordDao
import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.EmptyResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class WordLocalDataSourceImpl @Inject constructor(
    private val wordDao: WordDao
) : WordLocalDataSource {

    override fun getAllWords(): Flow<List<WordEntity>> = wordDao.getAllWords()


    override fun getWordsWithImages(): Flow<List<WordEntity>> = wordDao.getWordsWithImages()

    override suspend fun getWordById(
        wordId: Int
    ): LinguaQuestResult<WordEntity?, LinguaQuestDataError.Local> =
        safeDatabaseCall { wordDao.getWordById(wordId) }


    override suspend fun deleteWord(
        word: WordEntity
    ): EmptyResult<LinguaQuestDataError.Local> =
        safeDatabaseCall { wordDao.deleteWord(word) }


    override suspend fun deleteWordById(
        wordId: Int
    ): EmptyResult<LinguaQuestDataError.Local> =
        safeDatabaseCall { wordDao.deleteWordById(wordId) }


    override suspend fun setFavoriteStatus(
        wordId: Int,
        isFavorite: Boolean
    ): EmptyResult<LinguaQuestDataError.Local> =
        safeDatabaseCall { wordDao.setFavoriteStatus(wordId, isFavorite) }

}