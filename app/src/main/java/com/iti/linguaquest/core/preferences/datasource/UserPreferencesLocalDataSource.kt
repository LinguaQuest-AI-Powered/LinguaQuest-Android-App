package com.iti.linguaquest.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Interface
interface UserPreferencesLocalDataSource {
    val targetLanguage: Flow<String?>
    val nativeLanguage: Flow<String?>
    val proficiencyLevel: Flow<String?>
    val appTheme: Flow<String>

    suspend fun saveTargetLanguage(language: String)
    suspend fun saveNativeLanguage(language: String)
    suspend fun saveProficiencyLevel(level: String)
    suspend fun saveAppTheme(theme: String)
}

class UserPreferencesLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesLocalDataSource {

    override val targetLanguage: Flow<String?> = dataStore.data.map { it[PreferencesKeys.TARGET_LANGUAGE] }

    override val nativeLanguage: Flow<String?> = dataStore.data.map { it[PreferencesKeys.NATIVE_LANGUAGE] }

    override val proficiencyLevel: Flow<String?> = dataStore.data.map { it[PreferencesKeys.PROFICIENCY_LEVEL] }

    override val appTheme: Flow<String> = dataStore.data.map { it[PreferencesKeys.APP_THEME] ?: "system" }

    override suspend fun saveTargetLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TARGET_LANGUAGE] = language
        }
    }

    override suspend fun saveNativeLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NATIVE_LANGUAGE] = language
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
}