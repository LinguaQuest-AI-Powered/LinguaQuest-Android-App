package com.iti.linguaquest.features.lockscreen.domain.repository

import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureMetadata
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import kotlinx.coroutines.flow.Flow

import com.iti.linguaquest.features.lockscreen.domain.model.VocabularyBatchParams

interface LockScreenRepository {
    val featureEnabled: Flow<Boolean>
    val pendingGeneration: Flow<Boolean>
    val batchSize: Flow<Int>
    val lastGenerationTime: Flow<Long?>
    val lastNativeLanguage: Flow<String?>
    val lastTargetLanguage: Flow<String?>
    val lastProficiencyLevel: Flow<String?>
    val pendingOperationId: Flow<String?>
    val lastRewardedMilestoneCount: Flow<Int?>
    val pendingCount: Flow<Int>
    val allWords: Flow<List<LockScreenWord>>
    val pendingWord: Flow<LockScreenWord?>
    val postedOrOpenedWords: Flow<List<LockScreenWord>>

    suspend fun enable()
    suspend fun disable()
    suspend fun generateBatch(
        params: VocabularyBatchParams
    ): LinguaQuestResult<List<GeneratedVocabularyWord>, LinguaQuestDataError>
    suspend fun saveGeneratedBatch(
        words: List<GeneratedVocabularyWord>,
        params: VocabularyBatchParams
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
    suspend fun updateFeatureMetadata(metadata: LockScreenFeatureMetadata)
    suspend fun saveLastRewardedMilestoneCount(count: Int?)
    suspend fun recentGeneratedWords(limit: Int = 100): List<String>
    suspend fun pendingCountOnce(): Int
    suspend fun observePendingOnce(): LockScreenWord?
}
