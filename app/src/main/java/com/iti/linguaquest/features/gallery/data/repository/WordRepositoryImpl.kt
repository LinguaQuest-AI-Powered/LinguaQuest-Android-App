package com.iti.linguaquest.features.gallery.data.repository

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.EmptyResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.gallery.data.datasource.WordLocalDataSource
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class WordRepositoryImpl @Inject constructor(
    private val localDataSource: WordLocalDataSource
) : WordRepository {

    override fun getAllWords(): Flow<List<WordEntity>> = localDataSource.getAllWords()

    override fun getWordsWithImages(): Flow<List<WordEntity>> = localDataSource.getWordsWithImages()

    override suspend fun getWordById(
        wordId: Int
    ): LinguaQuestResult<WordEntity, LinguaQuestDataError.Local> {
        return when (val result = localDataSource.getWordById(wordId)) {
            is LinguaQuestResult.Success -> {
                result.data?.let { LinguaQuestResult.Success(it) }
                    ?: LinguaQuestResult.Failure(LinguaQuestDataError.Local.NOT_FOUND)
            }

            is LinguaQuestResult.Failure -> result
        }
    }


    override suspend fun deleteWord(
        word: WordEntity
    ): EmptyResult<LinguaQuestDataError.Local> =
        localDataSource.deleteWord(word)


    override suspend fun deleteWordById(
        wordId: Int
    ): EmptyResult<LinguaQuestDataError.Local> =
        localDataSource.deleteWordById(wordId)


    override suspend fun setFavoriteStatus(
        wordId: Int,
        isFavorite: Boolean
    ): EmptyResult<LinguaQuestDataError.Local> =
        localDataSource.setFavoriteStatus(wordId, isFavorite)

}