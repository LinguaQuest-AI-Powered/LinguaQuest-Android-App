package com.iti.linguaquest.features.lockscreen.data.local

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordDao
import com.iti.linguaquest.core.database.lockscreen.LockScreenWordEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow



class LockScreenLocalDataSourceImpl @Inject constructor(
    private val wordDao: LockScreenWordDao,
    private val preferences: LockScreenPreferencesLocalDataSource
) : LockScreenLocalDataSource {

    override val pendingCount: Flow<Int> = wordDao.pendingCount()
    override val featureEnabled: Flow<Boolean> = preferences.featureEnabled
    override val pendingGeneration: Flow<Boolean> = preferences.pendingGeneration
    override val batchSize: Flow<Int> = preferences.batchSize
    override val lastGenerationTime: Flow<Long?> = preferences.lastGenerationTime
    override val lastNativeLanguage: Flow<String?> = preferences.lastNativeLanguage
    override val lastTargetLanguage: Flow<String?> = preferences.lastTargetLanguage
    override val lastProficiencyLevel: Flow<String?> = preferences.lastProficiencyLevel
    override val pendingOperationId: Flow<String?> = preferences.pendingOperationId
    override val lastRewardedMilestoneCount: Flow<Int?> = preferences.lastRewardedMilestoneCount

    override fun allWords(): Flow<List<LockScreenWordEntity>> = wordDao.allWords()

    override fun pendingWord(): Flow<LockScreenWordEntity?> = wordDao.getPendingWord()

    override fun observeWord(wordId: Int): Flow<LockScreenWordEntity?> = wordDao.observeById(wordId)

    override fun postedOrOpenedWords(): Flow<List<LockScreenWordEntity>> = wordDao.getPostedOrOpenedWords()

    override suspend fun insertBatch(words: List<LockScreenWordEntity>) {
        wordDao.insertBatch(words)
    }

    override suspend fun getWord(wordId: Int): LockScreenWordEntity? = wordDao.getById(wordId)

    override suspend fun getPendingWordOnce(): LockScreenWordEntity? = wordDao.getPendingWordOnce()

    override suspend fun getRandomPendingWordOnce(): LockScreenWordEntity? = wordDao.getRandomPendingWordOnce()

    override suspend fun pendingCountOnce(): Int = wordDao.pendingCountOnce()

    override suspend fun getRecentWords(limit: Int): List<String> = wordDao.getRecentWords(limit)

    override suspend fun updateStatus(
        wordId: Int,
        status: String,
        postedAt: Long?,
        openedAt: Long?
    ) {
        wordDao.updateStatus(wordId, status, postedAt, openedAt)
    }

    override suspend fun clearAll() {
        wordDao.clearAll()
    }

    override suspend fun clearByTargetLanguage(targetLanguage: String) {
        wordDao.clearByTargetLanguage(targetLanguage)
    }

    override suspend fun clearByTargetLanguageAndLevel(targetLanguage: String, proficiencyLevel: String) {
        wordDao.clearByTargetLanguageAndLevel(targetLanguage, proficiencyLevel)
    }

    override suspend fun saveFeatureEnabled(enabled: Boolean) {
        preferences.saveFeatureEnabled(enabled)
    }

    override suspend fun savePendingGeneration(pending: Boolean) {
        preferences.savePendingGeneration(pending)
    }

    override suspend fun saveBatchSize(size: Int) {
        preferences.saveBatchSize(size)
    }

    override suspend fun saveLastGenerationTime(time: Long?) {
        preferences.saveLastGenerationTime(time)
    }

    override suspend fun saveLastNativeLanguage(language: String?) {
        preferences.saveLastNativeLanguage(language)
    }

    override suspend fun saveLastTargetLanguage(language: String?) {
        preferences.saveLastTargetLanguage(language)
    }

    override suspend fun saveLastProficiencyLevel(level: String?) {
        preferences.saveLastProficiencyLevel(level)
    }

    override suspend fun savePendingOperationId(operationId: String?) {
        preferences.savePendingOperationId(operationId)
    }

    override suspend fun saveLastRewardedMilestoneCount(count: Int?) {
        preferences.saveLastRewardedMilestoneCount(count)
    }

    override suspend fun clearFeatureMetadata() {
        preferences.clear()
    }
}
