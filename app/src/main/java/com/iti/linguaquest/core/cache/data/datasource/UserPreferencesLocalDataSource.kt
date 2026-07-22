package com.iti.linguaquest.core.cache.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.iti.linguaquest.core.di.UserSettingsDataStore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Interface
interface UserPreferencesLocalDataSource {
    val targetLanguage: Flow<Int?>
    val nativeLanguage: Flow<Int?>
    val proficiencyLevel: Flow<String?>
    val appTheme: Flow<String>
    val soundEnabled: Flow<Boolean>
    val appLanguage: Flow<String>
    val notificationsEnabled: Flow<Boolean>

    suspend fun saveTargetLanguage(languageId: Int)
    suspend fun saveNativeLanguage(languageId: Int)
    suspend fun saveProficiencyLevel(level: String)
    suspend fun saveAppTheme(theme: String)
    suspend fun saveSoundEnabled(enabled: Boolean)
    suspend fun saveAppLanguage(language: String)
    suspend fun saveNotificationsEnabled(enabled: Boolean)
    suspend fun clearOnboardingPreferences()
}

class UserPreferencesLocalDataSourceImpl @Inject constructor(
    @UserSettingsDataStore private val dataStore: DataStore<Preferences>
) : UserPreferencesLocalDataSource {

    override val targetLanguage: Flow<Int?> = dataStore.data.map { it[PreferencesKeys.TARGET_LANGUAGE] }

    override val nativeLanguage: Flow<Int?> = dataStore.data.map { it[PreferencesKeys.NATIVE_LANGUAGE] }

    override val proficiencyLevel: Flow<String?> = dataStore.data.map { it[PreferencesKeys.PROFICIENCY_LEVEL] }

    override val appTheme: Flow<String> = dataStore.data.map { it[PreferencesKeys.APP_THEME] ?: "system" }

    override val soundEnabled: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.SOUND_ENABLED] ?: true }

    override val appLanguage: Flow<String> = dataStore.data.map { it[PreferencesKeys.APP_LANGUAGE] ?: "en" }

    override val notificationsEnabled: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true }

    override suspend fun saveTargetLanguage(languageId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TARGET_LANGUAGE] = languageId
        }
    }

    override suspend fun saveNativeLanguage(languageId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NATIVE_LANGUAGE] = languageId
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

    override suspend fun saveNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    override suspend fun clearOnboardingPreferences() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.TARGET_LANGUAGE)
            preferences.remove(PreferencesKeys.NATIVE_LANGUAGE)
            preferences.remove(PreferencesKeys.PROFICIENCY_LEVEL)
        }
    }
}