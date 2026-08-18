package com.iti.linguaquest.features.gallery.data.repository

import com.iti.linguaquest.core.database.word.WordEntity
import com.iti.linguaquest.core.result.EmptyResult
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.gallery.data.datasource.WordLocalDataSource
import com.iti.linguaquest.features.gallery.data.datasource.remote.WordRemoteDataSource
import com.iti.linguaquest.features.gallery.data.mapper.toWordEntities
import com.iti.linguaquest.features.gallery.domain.repository.WordRepository
import com.iti.linguaquest.core.utils.VaultImageStorageManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class WordRepositoryImpl @Inject constructor(
    private val remoteDataSource: WordRemoteDataSource,
    private val localDataSource: WordLocalDataSource,
    private val vaultImageStorageManager: VaultImageStorageManager
) : WordRepository {

    override fun getAllWords(): Flow<List<WordEntity>> = localDataSource.getAllWords()

    override fun getWordsWithImages(): Flow<List<WordEntity>> = localDataSource.getWordsWithImages()

    override suspend fun refreshGalleryWords(
        sourceLanguage: String,
        targetLanguage: String,
        targetLanguageCode: String
    ): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return when (val result = remoteDataSource.getGalleryWords()) {
            is LinguaQuestResult.Success -> {
                val mappedWords = result.data.words.orEmpty().toWordEntities(
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage
                )
                
                val finalWords = mappedWords.map { word ->
                    val localUri = vaultImageStorageManager.getVaultImageUri(word.sourceWord, targetLanguageCode)
                    if (localUri != null) {
                        word.copy(imagePath = localUri)
                    } else {
                        word.copy(imagePath = "")
                    }
                }

                when (val saveResult = localDataSource.replaceWords(finalWords)) {
                    is LinguaQuestResult.Success -> LinguaQuestResult.Success(Unit)
                    is LinguaQuestResult.Failure -> saveResult
                }
            }
            is LinguaQuestResult.Failure -> result
        }
    }

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



    override suspend fun deleteWordById(
        wordId: Int
    ): EmptyResult<LinguaQuestDataError.Local> =
        localDataSource.deleteWordById(wordId)

    override suspend fun setCorrectStatus(
        wordId: Int,
        isCorrect: Boolean
    ): EmptyResult<LinguaQuestDataError.Local> =
        localDataSource.setCorrectStatus(wordId, isCorrect)
}