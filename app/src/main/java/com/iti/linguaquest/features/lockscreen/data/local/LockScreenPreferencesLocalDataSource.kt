package com.iti.linguaquest.features.lockscreen.data.local


import kotlinx.coroutines.flow.Flow
 interface LockScreenPreferencesLocalDataSource {
    val featureEnabled: Flow<Boolean>
    val pendingGeneration: Flow<Boolean>
    val batchSize: Flow<Int>
    val lastGenerationTime: Flow<Long?>
    val lastNativeLanguage: Flow<String?>
    val lastTargetLanguage: Flow<String?>
    val lastProficiencyLevel: Flow<String?>
    val pendingOperationId: Flow<String?>
    val lastRewardedMilestoneCount: Flow<Int?>

    suspend fun saveFeatureEnabled(enabled: Boolean)
    suspend fun savePendingGeneration(pending: Boolean)
    suspend fun saveBatchSize(size: Int)
    suspend fun saveLastGenerationTime(time: Long?)
    suspend fun saveLastNativeLanguage(language: String?)
    suspend fun saveLastTargetLanguage(language: String?)
    suspend fun saveLastProficiencyLevel(level: String?)
    suspend fun savePendingOperationId(operationId: String?)
    suspend fun saveLastRewardedMilestoneCount(count: Int?)
    suspend fun clear()
}
