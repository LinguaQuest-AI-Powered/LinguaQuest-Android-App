package com.iti.linguaquest.core.preferences

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
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

class UserPreferencesRepositoryImpl @Inject constructor(
    private val localDataSource: UserPreferencesLocalDataSource
) : UserPreferencesRepository {

    override val targetLanguage: Flow<String?> = localDataSource.targetLanguage
    override val proficiencyLevel: Flow<String?> = localDataSource.proficiencyLevel
    override val appTheme: Flow<String> = localDataSource.appTheme
    override val soundEnabled: Flow<Boolean> = localDataSource.soundEnabled
    override val appLanguage: Flow<String> = localDataSource.appLanguage

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
}