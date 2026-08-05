package com.iti.linguaquest.core.cache.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
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

    suspend fun saveTargetLanguage(languageId: Int, name: String)
    suspend fun saveNativeLanguage(languageId: Int, name: String)
    suspend fun saveProficiencyLevel(level: String)
    suspend fun saveAppTheme(theme: String)
    suspend fun saveSoundEnabled(enabled: Boolean)
    suspend fun saveAppLanguage(language: String)
    suspend fun saveNotificationsEnabled(enabled: Boolean)
    suspend fun saveReminderEnabled(enabled: Boolean)
    suspend fun saveReminderTime(time: String)
    suspend fun saveReminderDays(days: String)
    suspend fun clearTargetLanguage()
}
