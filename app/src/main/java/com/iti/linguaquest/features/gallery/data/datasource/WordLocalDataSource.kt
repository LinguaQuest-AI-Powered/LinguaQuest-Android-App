package com.iti.linguaquest.features.gallery.data.datasource

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.EmptyResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import kotlinx.coroutines.flow.Flow

interface WordLocalDataSource {
    fun getAllWords(): Flow<List<WordEntity>>
    fun getWordsWithImages(): Flow<List<WordEntity>>
    suspend fun getWordById(wordId: Int): LinguaQuestResult<WordEntity?, LinguaQuestDataError.Local>
    suspend fun deleteWord(word: WordEntity): EmptyResult<LinguaQuestDataError.Local>
    suspend fun deleteWordById(wordId: Int): EmptyResult<LinguaQuestDataError.Local>
    suspend fun setFavoriteStatus(
        wordId: Int,
        isFavorite: Boolean
    ): EmptyResult<LinguaQuestDataError.Local>
}