package com.iti.linguaquest.core.preferences

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

// Interface (DOMAIN LAYER)
interface UserPreferencesRepository {
    val targetLanguage: Flow<String?>
    val nativeLanguage: Flow<String?>
    val proficiencyLevel: Flow<String?>
    val appTheme: Flow<String>

    suspend fun saveTargetLanguage(language: String)
    suspend fun saveNativeLanguage(language: String)
    suspend fun saveProficiencyLevel(level: String)
    suspend fun saveAppTheme(theme: String)
}

class UserPreferencesRepositoryImpl @Inject constructor(
    private val localDataSource: UserPreferencesLocalDataSource
) : UserPreferencesRepository {

    override val targetLanguage: Flow<String?> = localDataSource.targetLanguage
    override val nativeLanguage: Flow<String?> = localDataSource.nativeLanguage
    override val proficiencyLevel: Flow<String?> = localDataSource.proficiencyLevel
    override val appTheme: Flow<String> = localDataSource.appTheme

    override suspend fun saveTargetLanguage(language: String) {
        localDataSource.saveTargetLanguage(language)
    }

    override suspend fun saveNativeLanguage(language: String) {
        localDataSource.saveNativeLanguage(language)
    }

    override suspend fun saveProficiencyLevel(level: String) {
        localDataSource.saveProficiencyLevel(level)
    }

    override suspend fun saveAppTheme(theme: String) {
        localDataSource.saveAppTheme(theme)
    }
}