package com.iti.linguaquest.core.cache.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.iti.linguaquest.core.di.SessionDataStore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SessionManagerDataSource {
    val isLoggedIn: Flow<Boolean>
    val firstTime: Flow<Boolean>

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean)
    suspend fun saveFirstTime(firstTime: Boolean)
}

class SessionManagerDataSourceImpl @Inject constructor(
    @SessionDataStore private val dataStore: DataStore<Preferences>
) : SessionManagerDataSource {

    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[SessionKeys.IS_LOGGED_IN] ?: false }

    override val firstTime: Flow<Boolean> = dataStore.data.map { it[SessionKeys.FIRST_TIME] ?: true }

    override suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[SessionKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }

    override suspend fun saveFirstTime(firstTime: Boolean) {
        dataStore.edit { preferences ->
            preferences[SessionKeys.FIRST_TIME] = firstTime
        }
    }
}
