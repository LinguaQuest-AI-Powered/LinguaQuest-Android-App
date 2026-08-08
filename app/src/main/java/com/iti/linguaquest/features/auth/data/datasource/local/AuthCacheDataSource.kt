package com.iti.linguaquest.features.auth.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iti.linguaquest.core.di.AuthCacheDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface AuthCacheDataSource {
    val cachedAuthLanguages: Flow<String?>
    suspend fun saveCachedAuthLanguages(json: String)
}

class AuthCacheDataSourceImpl @Inject constructor(
    @AuthCacheDataStore private val dataStore: DataStore<Preferences>
) : AuthCacheDataSource {

    private object Keys {
        val CACHED_AUTH_LANGUAGES = stringPreferencesKey("cached_auth_languages")
    }

    override val cachedAuthLanguages: Flow<String?> =
        dataStore.data.map { it[Keys.CACHED_AUTH_LANGUAGES] }

    override suspend fun saveCachedAuthLanguages(json: String) {
        dataStore.edit { preferences ->
            preferences[Keys.CACHED_AUTH_LANGUAGES] = json
        }
    }
}

