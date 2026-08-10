package com.iti.linguaquest.features.lockscreen.data.local
import com.iti.linguaquest.core.database.lockscreen.LockScreenWordEntity
import kotlinx.coroutines.flow.Flow

interface LockScreenLocalDataSource {
    val featureEnabled: Flow<Boolean>
    val pendingGeneration: Flow<Boolean>
    val batchSize: Flow<Int>
    val lastGenerationTime: Flow<Long?>
    val lastNativeLanguage: Flow<String?>
    val lastTargetLanguage: Flow<String?>
    val lastProficiencyLevel: Flow<String?>
    val pendingOperationId: Flow<String?>
    val lastRewardedMilestoneCount: Flow<Int?>

    fun allWords(userId: Int): Flow<List<LockScreenWordEntity>>
    fun pendingWord(userId: Int): Flow<LockScreenWordEntity?>
    fun observeWord(wordId: Int): Flow<LockScreenWordEntity?>
    fun postedOrOpenedWords(userId: Int): Flow<List<LockScreenWordEntity>>

    suspend fun insertBatch(words: List<LockScreenWordEntity>)
    suspend fun getWord(wordId: Int): LockScreenWordEntity?
    suspend fun getPendingWordOnce(userId: Int): LockScreenWordEntity?
    suspend fun getRandomPendingWordOnce(userId: Int): LockScreenWordEntity?
    
    fun pendingCount(userId: Int): Flow<Int>
    suspend fun pendingCountOnce(userId: Int): Int
    suspend fun getRecentWords(userId: Int, limit: Int): List<String>
    
    suspend fun updateStatus(wordId: Int, status: String, postedAt: Long? = null, openedAt: Long? = null)
    suspend fun clearAll(userId: Int)
    suspend fun clearByTargetLanguage(userId: Int, targetLanguage: String)
    suspend fun clearByTargetLanguageAndLevel(userId: Int, targetLanguage: String, proficiencyLevel: String)

    suspend fun saveFeatureEnabled(enabled: Boolean)
    suspend fun savePendingGeneration(pending: Boolean)
    suspend fun saveBatchSize(size: Int)
    suspend fun saveLastGenerationTime(time: Long?)
    suspend fun saveLastNativeLanguage(language: String?)
    suspend fun saveLastTargetLanguage(language: String?)
    suspend fun saveLastProficiencyLevel(level: String?)
    suspend fun savePendingOperationId(operationId: String?)
    suspend fun saveLastRewardedMilestoneCount(count: Int?)
    suspend fun clearFeatureMetadata()
}
