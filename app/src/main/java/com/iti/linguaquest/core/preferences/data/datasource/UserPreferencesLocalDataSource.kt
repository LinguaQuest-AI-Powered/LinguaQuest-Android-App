package com.iti.linguaquest.core.preferences.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Interface
interface UserPreferencesLocalDataSource {
    val targetLanguage: Flow<String?>
    val proficiencyLevel: Flow<String?>
    val appTheme: Flow<String>
    val soundEnabled: Flow<Boolean>
    val appLanguage: Flow<String>

    suspend fun saveTargetLanguage(language: String)
    suspend fun saveProficiencyLevel(level: String)
    suspend fun saveAppTheme(theme: String)
    suspend fun saveSoundEnabled(enabled: Boolean)
    suspend fun saveAppLanguage(language: String)
}

class UserPreferencesLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesLocalDataSource {

    override val targetLanguage: Flow<String?> = dataStore.data.map { it[PreferencesKeys.TARGET_LANGUAGE] }

    override val proficiencyLevel: Flow<String?> = dataStore.data.map { it[PreferencesKeys.PROFICIENCY_LEVEL] }

    override val appTheme: Flow<String> = dataStore.data.map { it[PreferencesKeys.APP_THEME] ?: "system" }

    override val soundEnabled: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.SOUND_ENABLED] ?: true }

    override val appLanguage: Flow<String> = dataStore.data.map { it[PreferencesKeys.APP_LANGUAGE] ?: "en" }

    override suspend fun saveTargetLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TARGET_LANGUAGE] = language
        }
    }


    override suspend fun saveProficiencyLevel(level: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROFICIENCY_LEVEL] = level
        }
    }

    override suspend fun saveAppTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_THEME] = theme
        }
    }

    override suspend fun saveSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    override suspend fun saveAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] = language
        }
    }
}