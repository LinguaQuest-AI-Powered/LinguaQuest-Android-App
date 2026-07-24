package com.iti.linguaquest.core.cache.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.iti.linguaquest.core.di.UserSettingsDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Interface
interface UserPreferencesLocalDataSource {
    val targetLanguage: Flow<Int?>
    val targetLanguageName: Flow<String?>
    val nativeLanguage: Flow<Int?>
    val nativeLanguageName: Flow<String?>
    val proficiencyLevel: Flow<String?>
    val appTheme: Flow<String>
    val soundEnabled: Flow<Boolean>
    val appLanguage: Flow<String>
    val notificationsEnabled: Flow<Boolean>
    val reminderEnabled: Flow<Boolean>
    val reminderTime: Flow<String>
    val reminderDays: Flow<String>

    suspend fun saveTargetLanguage(languageId: Int)
    suspend fun saveTargetLanguageName(name: String)
    suspend fun saveNativeLanguage(languageId: Int)
    suspend fun saveNativeLanguageName(name: String)
    suspend fun saveProficiencyLevel(level: String)
    suspend fun saveAppTheme(theme: String)
    suspend fun saveSoundEnabled(enabled: Boolean)
    suspend fun saveAppLanguage(language: String)
    suspend fun saveNotificationsEnabled(enabled: Boolean)
    suspend fun saveReminderEnabled(enabled: Boolean)
    suspend fun saveReminderTime(time: String)
    suspend fun saveReminderDays(days: String)
    suspend fun clearOnboardingPreferences()
}

class UserPreferencesLocalDataSourceImpl @Inject constructor(
    @UserSettingsDataStore private val dataStore: DataStore<Preferences>
) : UserPreferencesLocalDataSource {

    override val targetLanguage: Flow<Int?> = dataStore.data.map { it[PreferencesKeys.TARGET_LANGUAGE] }
    override val targetLanguageName: Flow<String?> = dataStore.data.map { it[PreferencesKeys.TARGET_LANGUAGE_NAME] }

    override val nativeLanguage: Flow<Int?> = dataStore.data.map { it[PreferencesKeys.NATIVE_LANGUAGE] }
    override val nativeLanguageName: Flow<String?> = dataStore.data.map { it[PreferencesKeys.NATIVE_LANGUAGE_NAME] }

    override val proficiencyLevel: Flow<String?> = dataStore.data.map { it[PreferencesKeys.PROFICIENCY_LEVEL] }

    override val appTheme: Flow<String> = dataStore.data.map { it[PreferencesKeys.APP_THEME] ?: "system" }

    override val soundEnabled: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.SOUND_ENABLED] ?: true }

    override val appLanguage: Flow<String> = dataStore.data.map { it[PreferencesKeys.APP_LANGUAGE] ?: "en" }

    override val notificationsEnabled: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true }

    override val reminderEnabled: Flow<Boolean> = dataStore.data.map { it[PreferencesKeys.REMINDER_ENABLED] ?: false }

    override val reminderTime: Flow<String> = dataStore.data.map { it[PreferencesKeys.REMINDER_TIME] ?: "08:00" }

    override val reminderDays: Flow<String> = dataStore.data.map { it[PreferencesKeys.REMINDER_DAYS] ?: "1,2,3,4,5,6,7" }

     override suspend fun saveTargetLanguage(languageId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TARGET_LANGUAGE] = languageId
        }
    }

    override suspend fun saveTargetLanguageName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TARGET_LANGUAGE_NAME] = name
        }
    }

    override suspend fun saveNativeLanguage(languageId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NATIVE_LANGUAGE] = languageId
        }
    }

    override suspend fun saveNativeLanguageName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NATIVE_LANGUAGE_NAME] = name
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

    override suspend fun saveReminderEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_ENABLED] = enabled
        }
    }

    override suspend fun saveReminderTime(time: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_TIME] = time
        }
    }

    override suspend fun saveReminderDays(days: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMINDER_DAYS] = days
        }
    }

    override suspend fun clearOnboardingPreferences() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.TARGET_LANGUAGE)
            preferences.remove(PreferencesKeys.TARGET_LANGUAGE_NAME)
            preferences.remove(PreferencesKeys.NATIVE_LANGUAGE)
            preferences.remove(PreferencesKeys.NATIVE_LANGUAGE_NAME)
            preferences.remove(PreferencesKeys.PROFICIENCY_LEVEL)
        }
    }
}