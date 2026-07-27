package com.iti.linguaquest.features.lockscreen.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


import com.iti.linguaquest.core.di.UserSettingsDataStore

class LockScreenPreferencesLocalDataSourceImpl @Inject constructor(
    @UserSettingsDataStore private val dataStore: DataStore<Preferences>
) : LockScreenPreferencesLocalDataSource {

    private object Keys {
        val FEATURE_ENABLED = booleanPreferencesKey("lockscreen_feature_enabled")
        val PENDING_GENERATION = booleanPreferencesKey("lockscreen_pending_generation")
        val BATCH_SIZE = androidx.datastore.preferences.core.intPreferencesKey("lockscreen_batch_size")
        val LAST_GENERATION_TIME = longPreferencesKey("lockscreen_last_generation_time")
        val LAST_NATIVE_LANGUAGE = stringPreferencesKey("lockscreen_last_native_language")
        val LAST_TARGET_LANGUAGE = stringPreferencesKey("lockscreen_last_target_language")
        val LAST_PROFICIENCY_LEVEL = stringPreferencesKey("lockscreen_last_proficiency_level")
        val PENDING_OPERATION_ID = stringPreferencesKey("lockscreen_pending_operation_id")
        val LAST_REWARDED_MILESTONE_COUNT = intPreferencesKey("lockscreen_last_rewarded_milestone_count")
    }

    override val featureEnabled: Flow<Boolean> =
        dataStore.data.map { it[Keys.FEATURE_ENABLED] ?: false }

    override val pendingGeneration: Flow<Boolean> =
        dataStore.data.map { it[Keys.PENDING_GENERATION] ?: false }

    override val batchSize: Flow<Int> =
        dataStore.data.map { it[Keys.BATCH_SIZE] ?: 30 }

    override val lastGenerationTime: Flow<Long?> =
        dataStore.data.map { it[Keys.LAST_GENERATION_TIME] }

    override val lastNativeLanguage: Flow<String?> =
        dataStore.data.map { it[Keys.LAST_NATIVE_LANGUAGE] }

    override val lastTargetLanguage: Flow<String?> =
        dataStore.data.map { it[Keys.LAST_TARGET_LANGUAGE] }

    override val lastProficiencyLevel: Flow<String?> =
        dataStore.data.map { it[Keys.LAST_PROFICIENCY_LEVEL] }

    override val pendingOperationId: Flow<String?> =
        dataStore.data.map { it[Keys.PENDING_OPERATION_ID] }

    override val lastRewardedMilestoneCount: Flow<Int?> =
        dataStore.data.map { it[Keys.LAST_REWARDED_MILESTONE_COUNT] }

    override suspend fun saveFeatureEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.FEATURE_ENABLED] = enabled }
    }

    override suspend fun savePendingGeneration(pending: Boolean) {
        dataStore.edit { it[Keys.PENDING_GENERATION] = pending }
    }

    override suspend fun saveBatchSize(size: Int) {
        dataStore.edit { it[Keys.BATCH_SIZE] = size }
    }

    override suspend fun saveLastGenerationTime(time: Long?) {
        dataStore.edit { prefs ->
            if (time == null) prefs.remove(Keys.LAST_GENERATION_TIME) else prefs[Keys.LAST_GENERATION_TIME] = time
        }
    }

    override suspend fun saveLastNativeLanguage(language: String?) {
        dataStore.edit { prefs ->
            if (language == null) prefs.remove(Keys.LAST_NATIVE_LANGUAGE) else prefs[Keys.LAST_NATIVE_LANGUAGE] = language
        }
    }

    override suspend fun saveLastTargetLanguage(language: String?) {
        dataStore.edit { prefs ->
            if (language == null) prefs.remove(Keys.LAST_TARGET_LANGUAGE) else prefs[Keys.LAST_TARGET_LANGUAGE] = language
        }
    }

    override suspend fun saveLastProficiencyLevel(level: String?) {
        dataStore.edit { prefs ->
            if (level == null) prefs.remove(Keys.LAST_PROFICIENCY_LEVEL) else prefs[Keys.LAST_PROFICIENCY_LEVEL] = level
        }
    }

    override suspend fun savePendingOperationId(operationId: String?) {
        dataStore.edit { prefs ->
            if (operationId == null) prefs.remove(Keys.PENDING_OPERATION_ID) else prefs[Keys.PENDING_OPERATION_ID] = operationId
        }
    }

    override suspend fun saveLastRewardedMilestoneCount(count: Int?) {
        dataStore.edit { prefs ->
            if (count == null) {
                prefs.remove(Keys.LAST_REWARDED_MILESTONE_COUNT)
            } else {
                prefs[Keys.LAST_REWARDED_MILESTONE_COUNT] = count
            }
        }
    }

    override suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.FEATURE_ENABLED)
            prefs.remove(Keys.PENDING_GENERATION)
            prefs.remove(Keys.BATCH_SIZE)
            prefs.remove(Keys.LAST_GENERATION_TIME)
            prefs.remove(Keys.LAST_NATIVE_LANGUAGE)
            prefs.remove(Keys.LAST_TARGET_LANGUAGE)
            prefs.remove(Keys.LAST_PROFICIENCY_LEVEL)
            prefs.remove(Keys.PENDING_OPERATION_ID)
            prefs.remove(Keys.LAST_REWARDED_MILESTONE_COUNT)
        }
    }
}
