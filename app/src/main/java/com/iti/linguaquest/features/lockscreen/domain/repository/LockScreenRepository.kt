package com.iti.linguaquest.features.lockscreen.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import kotlinx.coroutines.flow.Flow

interface LockScreenRepository {
    val featureEnabled: Flow<Boolean>
    val pendingGeneration: Flow<Boolean>
    val batchSize: Flow<Int>
    val lastGenerationTime: Flow<Long?>
    val lastNativeLanguage: Flow<String?>
    val lastTargetLanguage: Flow<String?>
    val lastProficiencyLevel: Flow<String?>
    val pendingOperationId: Flow<String?>
    val pendingCount: Flow<Int>
    val allWords: Flow<List<LockScreenWord>>
    val pendingWord: Flow<LockScreenWord?>

    suspend fun enable()
    suspend fun disable()
    suspend fun deduceCoinsAndEnable(operationId: String, amount: Int = 50): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun generateBatch(): LinguaQuestResult<Int, LinguaQuestDataError>
    suspend fun generateBatch(
        batchSize: Int,
        excludeWords: List<String>,
        nativeLanguage: String,
        targetLanguage: String,
        proficiencyLevel: String
    ): LinguaQuestResult<List<GeneratedVocabularyWord>, LinguaQuestDataError>
    suspend fun saveGeneratedBatch(
        words: List<GeneratedVocabularyWord>,
        nativeLanguage: String,
        targetLanguage: String,
        proficiencyLevel: String
    ): LinguaQuestResult<Int, LinguaQuestDataError>
    suspend fun getWordById(wordId: Int): LockScreenWord?
    suspend fun markPosted(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun markFailed(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun markOpened(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun clearWords(): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun clearWordsForLanguage(targetLanguage: String): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun clearWordsForLanguageAndLevel(
        targetLanguage: String,
        proficiencyLevel: String
    ): LinguaQuestResult<Unit, LinguaQuestDataError>
    suspend fun updateFeatureMetadata(
        enabled: Boolean,
        pendingGeneration: Boolean,
        operationId: String? = null,
        batchSize: Int? = null,
        lastGenerationTime: Long? = null,
        lastNativeLanguage: String? = null,
        lastTargetLanguage: String? = null,
        lastProficiencyLevel: String? = null
    )
    suspend fun recentGeneratedWords(limit: Int = 100): List<String>
    suspend fun pendingCountOnce(): Int
    suspend fun observePendingOnce(): LockScreenWord?
}
