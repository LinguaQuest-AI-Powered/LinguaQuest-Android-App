package com.iti.linguaquest.features.profile.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val AVATAR_URL_KEY = stringPreferencesKey("profile_avatar_url_cache")

interface ProfileLocalDataSource {
    val cachedAvatarUrl: Flow<String?>
    suspend fun saveAvatarUrl(url: String)
}

class ProfileLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ProfileLocalDataSource {

    override val cachedAvatarUrl: Flow<String?> =
        dataStore.data.map { it[AVATAR_URL_KEY] }

    override suspend fun saveAvatarUrl(url: String) {
        dataStore.edit { it[AVATAR_URL_KEY] = url }
    }
}