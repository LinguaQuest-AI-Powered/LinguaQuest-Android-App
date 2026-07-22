package com.iti.linguaquest.core.cache.data.repository

import com.iti.linguaquest.core.cache.data.datasource.UserPreferencesLocalDataSource
import com.iti.linguaquest.core.cache.domain.repository.UserPreferencesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl @Inject constructor(
    private val localDataSource: UserPreferencesLocalDataSource
) : UserPreferencesRepository {

    override val targetLanguage: Flow<Int?> = localDataSource.targetLanguage
    override val nativeLanguage: Flow<Int?> = localDataSource.nativeLanguage
    override val proficiencyLevel: Flow<String?> = localDataSource.proficiencyLevel
    override val appTheme: Flow<String> = localDataSource.appTheme
    override val soundEnabled: Flow<Boolean> = localDataSource.soundEnabled
    override val appLanguage: Flow<String> = localDataSource.appLanguage
    override val notificationsEnabled: Flow<Boolean> = localDataSource.notificationsEnabled

    override suspend fun saveTargetLanguage(languageId: Int) {
        localDataSource.saveTargetLanguage(languageId)
    }

    override suspend fun saveNativeLanguage(languageId: Int) {
        localDataSource.saveNativeLanguage(languageId)
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
}
