package com.iti.linguaquest.core.cache.data.repository

import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl @Inject constructor(
    private val localDataSource: UserPreferencesLocalDataSource
) : UserPreferencesRepository {

    override val targetLanguage: Flow<Int?> = localDataSource.targetLanguage
    override val targetLanguageName: Flow<String?> = localDataSource.targetLanguageName
    override val targetLanguageCode: Flow<String?> = localDataSource.targetLanguageCode

    override val nativeLanguage: Flow<Int?> = localDataSource.nativeLanguage
    override val nativeLanguageName: Flow<String?> = localDataSource.nativeLanguageName
    override val nativeLanguageCode: Flow<String?> = localDataSource.nativeLanguageCode

    override val proficiencyLevel: Flow<String?> = localDataSource.proficiencyLevel
    override val appTheme: Flow<String> = localDataSource.appTheme
    override val soundEnabled: Flow<Boolean> = localDataSource.soundEnabled
    override val appLanguage: Flow<String> = localDataSource.appLanguage
    override val notificationsEnabled: Flow<Boolean> = localDataSource.notificationsEnabled
    override val reminderEnabled: Flow<Boolean> = localDataSource.reminderEnabled
    override val reminderTime: Flow<String> = localDataSource.reminderTime
    override val reminderDays: Flow<String> = localDataSource.reminderDays

    override suspend fun saveTargetLanguage(languageId: Int, name: String, code: String) {
        localDataSource.saveTargetLanguage(languageId)
        localDataSource.saveTargetLanguageName(name)
        localDataSource.saveTargetLanguageCode(code)
    }

    override suspend fun saveNativeLanguage(languageId: Int, name: String, code: String) {
        localDataSource.saveNativeLanguage(languageId)
        localDataSource.saveNativeLanguageName(name)
        localDataSource.saveNativeLanguageCode(code)
    }

    override suspend fun saveProficiencyLevel(level: String) {
        localDataSource.saveProficiencyLevel(level)
    }

    override suspend fun saveAppTheme(theme: String) {
        localDataSource.saveAppTheme(theme)
    }

    override suspend fun saveSoundEnabled(enabled: Boolean) {
        localDataSource.saveSoundEnabled(enabled)
    }

    override suspend fun saveAppLanguage(language: String) {
        localDataSource.saveAppLanguage(language)
    }

    override suspend fun saveNotificationsEnabled(enabled: Boolean) {
        localDataSource.saveNotificationsEnabled(enabled)
    }

    override suspend fun saveReminderEnabled(enabled: Boolean) {
        localDataSource.saveReminderEnabled(enabled)
    }

    override suspend fun saveReminderTime(time: String) {
        localDataSource.saveReminderTime(time)
    }

    override suspend fun saveReminderDays(days: String) {
        localDataSource.saveReminderDays(days)
    }

    override suspend fun clearTargetLanguage() {
        localDataSource.clearTargetLanguage()
    }

    override suspend fun clearOnboardingPreferences() {
        localDataSource.clearOnboardingPreferences()
    }
}
