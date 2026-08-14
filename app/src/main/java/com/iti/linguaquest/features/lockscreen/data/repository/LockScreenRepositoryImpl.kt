package com.iti.linguaquest.features.lockscreen.data.repository

import com.iti.linguaquest.core.database.lockscreen.LockScreenWordStatus
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult
import com.iti.linguaquest.features.lockscreen.data.local.LockScreenLocalDataSource
import com.iti.linguaquest.features.lockscreen.data.mapper.toDomain
import com.iti.linguaquest.features.lockscreen.data.mapper.toEntity
import com.iti.linguaquest.features.lockscreen.data.remote.LockScreenRemoteDataSource
import com.iti.linguaquest.features.lockscreen.domain.model.GeneratedVocabularyWord
import com.iti.linguaquest.features.lockscreen.domain.model.VocabularyBatchParams
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenFeatureMetadata
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import com.iti.linguaquest.features.lockscreen.domain.repository.LockScreenRepository
import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import com.iti.linguaquest.core.cache.data.datasource.SessionManagerDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first

class LockScreenRepositoryImpl @Inject constructor(
    private val remoteDataSource: LockScreenRemoteDataSource,
    private val localDataSource: LockScreenLocalDataSource,
    private val sessionManagerDataSource: SessionManagerDataSource,
    private val userPreferencesRepository: UserPreferencesRepository
) : LockScreenRepository {

    override val featureEnabled: Flow<Boolean> = localDataSource.featureEnabled
    override val pendingGeneration: Flow<Boolean> = localDataSource.pendingGeneration
    override val batchSize: Flow<Int> = localDataSource.batchSize
    override val lastGenerationTime: Flow<Long?> = localDataSource.lastGenerationTime
    override val lastNativeLanguage: Flow<String?> = localDataSource.lastNativeLanguage
    override val lastTargetLanguage: Flow<String?> = localDataSource.lastTargetLanguage
    override val lastProficiencyLevel: Flow<String?> = localDataSource.lastProficiencyLevel
    override val pendingOperationId: Flow<String?> = localDataSource.pendingOperationId
    override val lastRewardedMilestoneCount: Flow<Int?> = localDataSource.lastRewardedMilestoneCount
    @OptIn(ExperimentalCoroutinesApi::class)
    override val pendingCount: Flow<Int> = combine(
        sessionManagerDataSource.lastLoggedInUserId,
        userPreferencesRepository.targetLanguageName
    ) { userId, targetLanguage ->
        (userId ?: -1) to (targetLanguage.orEmpty().ifBlank { "English" })
    }.flatMapLatest { (userId, targetLanguage) ->
        localDataSource.pendingCount(userId, targetLanguage)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val allWords: Flow<List<LockScreenWord>> = sessionManagerDataSource.lastLoggedInUserId.flatMapLatest { userId -> localDataSource.allWords(userId ?: -1) }.map { list -> list.map { it.toDomain() } }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val pendingWord: Flow<LockScreenWord?> = combine(
        sessionManagerDataSource.lastLoggedInUserId,
        userPreferencesRepository.targetLanguageName
    ) { userId, targetLanguage ->
        (userId ?: -1) to (targetLanguage.orEmpty().ifBlank { "English" })
    }.flatMapLatest { (userId, targetLanguage) ->
        localDataSource.pendingWord(userId, targetLanguage)
    }.map { it?.toDomain() }
    override val postedOrOpenedWords: Flow<List<LockScreenWord>> = sessionManagerDataSource.lastLoggedInUserId.flatMapLatest { userId -> localDataSource.postedOrOpenedWords(userId ?: -1) }.map { list -> list.map { it.toDomain() } }

    override suspend fun enable() {
        localDataSource.saveFeatureEnabled(true)
        localDataSource.savePendingGeneration(true)
    }

    override suspend fun disable() {
        localDataSource.clearFeatureMetadata()
    }

    override suspend fun generateBatch(
        params: VocabularyBatchParams
    ): LinguaQuestResult<List<GeneratedVocabularyWord>, LinguaQuestDataError> {
        return remoteDataSource.generateVocabulary(params)
    }

    override suspend fun saveGeneratedBatch(
        words: List<GeneratedVocabularyWord>,
        params: VocabularyBatchParams
    ): LinguaQuestResult<Int, LinguaQuestDataError> {
        val userId = sessionManagerDataSource.getCurrentUserId()
        val entities = words.distinctBy { it.word.lowercase() }
            .map { it.toEntity(params.nativeLanguage, params.targetLanguage, params.proficiencyLevel, userId) }
        return try {
            localDataSource.insertBatch(entities)
            localDataSource.saveFeatureEnabled(true)
            localDataSource.savePendingGeneration(false)
            localDataSource.saveLastGenerationTime(System.currentTimeMillis())
            localDataSource.saveLastNativeLanguage(params.nativeLanguage)
            localDataSource.saveLastTargetLanguage(params.targetLanguage)
            localDataSource.saveLastProficiencyLevel(params.proficiencyLevel)
            LinguaQuestResult.Success(entities.size)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun getWordById(wordId: Int): LockScreenWord? {
        return localDataSource.getWord(wordId)?.toDomain()
    }

    override suspend fun markPosted(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.updateStatus(
                wordId = wordId,
                status = LockScreenWordStatus.POSTED.name,
                postedAt = System.currentTimeMillis()
            )
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun markFailed(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.updateStatus(
                wordId = wordId,
                status = LockScreenWordStatus.FAILED.name
            )
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun markOpened(wordId: Int): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            localDataSource.updateStatus(
                wordId = wordId,
                status = LockScreenWordStatus.OPENED.name,
                openedAt = System.currentTimeMillis()
            )
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun clearWords(): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            val userId = sessionManagerDataSource.getCurrentUserId()
            localDataSource.clearAll(userId)
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun clearWordsForLanguage(targetLanguage: String): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            val userId = sessionManagerDataSource.getCurrentUserId()
            localDataSource.clearByTargetLanguage(userId, targetLanguage)
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun clearWordsForLanguageAndLevel(
        targetLanguage: String,
        proficiencyLevel: String
    ): LinguaQuestResult<Unit, LinguaQuestDataError> {
        return try {
            val userId = sessionManagerDataSource.getCurrentUserId()
            localDataSource.clearByTargetLanguageAndLevel(userId, targetLanguage, proficiencyLevel)
            LinguaQuestResult.Success(Unit)
        } catch (e: Exception) {
             LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
        }
    }

    override suspend fun updateFeatureMetadata(metadata: LockScreenFeatureMetadata) {
        localDataSource.saveFeatureEnabled(metadata.enabled)
        localDataSource.savePendingGeneration(metadata.pendingGeneration)
        localDataSource.savePendingOperationId(metadata.operationId)
        if (metadata.batchSize != null) localDataSource.saveBatchSize(metadata.batchSize)
        localDataSource.saveLastGenerationTime(metadata.lastGenerationTime)
        localDataSource.saveLastNativeLanguage(metadata.lastNativeLanguage)
        localDataSource.saveLastTargetLanguage(metadata.lastTargetLanguage)
        localDataSource.saveLastProficiencyLevel(metadata.lastProficiencyLevel)
    }

    override suspend fun saveLastRewardedMilestoneCount(count: Int?) {
        localDataSource.saveLastRewardedMilestoneCount(count)
    }

    override suspend fun recentGeneratedWords(limit: Int): List<String> {
        val userId = sessionManagerDataSource.getCurrentUserId()
        return localDataSource.getRecentWords(userId, limit)
    }

    override suspend fun pendingCountOnce(): Int {
        val userId = sessionManagerDataSource.getCurrentUserId()
        val targetLanguage = userPreferencesRepository.targetLanguageName.first().orEmpty().ifBlank { "English" }
        return localDataSource.pendingCountOnce(userId, targetLanguage)
    }

    override suspend fun observePendingOnce(): LockScreenWord? {
        val userId = sessionManagerDataSource.getCurrentUserId()
        val targetLanguage = userPreferencesRepository.targetLanguageName.first().orEmpty().ifBlank { "English" }
        return localDataSource.getRandomPendingWordOnce(userId, targetLanguage)?.toDomain()
    }

}
