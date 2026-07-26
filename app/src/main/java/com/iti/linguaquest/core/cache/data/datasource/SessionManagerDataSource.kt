package com.iti.linguaquest.core.cache.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.iti.linguaquest.core.database.AppDatabase
import com.iti.linguaquest.core.di.SessionDataStore
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface SessionManagerDataSource {
    val isLoggedIn: Flow<Boolean>
    val firstTime: Flow<Boolean>

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean)
    suspend fun saveFirstTime(firstTime: Boolean)
    suspend fun clearSessionData()
}

class SessionManagerDataSourceImpl @Inject constructor(
    @SessionDataStore private val dataStore: DataStore<Preferences>,
    private val appDatabase: AppDatabase
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

    override suspend fun clearSessionData() {
        withContext(Dispatchers.IO) {
            appDatabase.clearAllTables()
        }
    }
}
