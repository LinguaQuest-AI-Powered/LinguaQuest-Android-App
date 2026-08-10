package com.iti.linguaquest.core.language.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iti.linguaquest.core.di.SupportedLanguagesCacheDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SupportedLanguagesCacheDataSource {
    val cachedSupportedLanguages: Flow<String?>
    suspend fun saveCachedSupportedLanguages(json: String)
}

class SupportedLanguagesCacheDataSourceImpl @Inject constructor(
    @SupportedLanguagesCacheDataStore private val dataStore: DataStore<Preferences>
) : SupportedLanguagesCacheDataSource {

    private object Keys {
        val CACHED_SUPPORTED_LANGUAGES = stringPreferencesKey("cached_supported_languages")
    }

    override val cachedSupportedLanguages: Flow<String?> =
        dataStore.data.map { it[Keys.CACHED_SUPPORTED_LANGUAGES] }

    override suspend fun saveCachedSupportedLanguages(json: String) {
        dataStore.edit { preferences ->
            preferences[Keys.CACHED_SUPPORTED_LANGUAGES] = json
        }
    }
}
