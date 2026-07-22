package com.iti.linguaquest.core.preferences.data.repository

import com.iti.linguaquest.core.preferences.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.preferences.domain.repository.UserPreferencesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl @Inject constructor(
    private val localDataSource: UserPreferencesLocalDataSource
) : UserPreferencesRepository {

    override val targetLanguage: Flow<String?> = localDataSource.targetLanguage
    override val proficiencyLevel: Flow<String?> = localDataSource.proficiencyLevel
    override val appTheme: Flow<String> = localDataSource.appTheme
    override val soundEnabled: Flow<Boolean> = localDataSource.soundEnabled
    override val appLanguage: Flow<String> = localDataSource.appLanguage
    override val notificationsEnabled: Flow<Boolean> = localDataSource.notificationsEnabled
    override val reminderEnabled: Flow<Boolean> = localDataSource.reminderEnabled
    override val reminderTime: Flow<String> = localDataSource.reminderTime
    override val reminderDays: Flow<String> = localDataSource.reminderDays

    override suspend fun saveTargetLanguage(language: String) {
        localDataSource.saveTargetLanguage(language)
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
}
