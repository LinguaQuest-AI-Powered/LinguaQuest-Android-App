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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

interface SessionManagerDataSource {
    val isLoggedIn: Flow<Boolean>
    val firstTime: Flow<Boolean>
    val lastLoggedInUserId: Flow<Int?>

    suspend fun saveIsLoggedIn(isLoggedIn: Boolean)
    suspend fun saveFirstTime(firstTime: Boolean)
    suspend fun saveLastLoggedInUserId(userId: Int)
    suspend fun getCurrentUserId(): Int
    suspend fun clearSessionData()
    suspend fun clearLocalGeneratedData()
    suspend fun clearLanguageDependentData()
}

class SessionManagerDataSourceImpl @Inject constructor(
    @SessionDataStore private val dataStore: DataStore<Preferences>,
    private val appDatabase: AppDatabase
) : SessionManagerDataSource {

    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[SessionKeys.IS_LOGGED_IN] ?: false }

    override val firstTime: Flow<Boolean> = dataStore.data.map { it[SessionKeys.FIRST_TIME] ?: true }

    override val lastLoggedInUserId: Flow<Int?> = dataStore.data.map { it[SessionKeys.LAST_LOGGED_IN_USER_ID] }

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

    override suspend fun saveLastLoggedInUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[SessionKeys.LAST_LOGGED_IN_USER_ID] = userId
        }
    }

    override suspend fun getCurrentUserId(): Int {
        return lastLoggedInUserId.first() ?: -1
    }

    override suspend fun clearSessionData() {
        withContext(Dispatchers.IO) {
            appDatabase.wordDao().clearWords()
            appDatabase.profileDao().clearProfile()
            appDatabase.homeDao().clearHomeSummary()
            appDatabase.notificationDao().deleteAllNotifications()
        }
    }

    override suspend fun clearLocalGeneratedData() {
        withContext(Dispatchers.IO) {
            appDatabase.wordDao().clearWords()
        }
    }

    override suspend fun clearLanguageDependentData() {
        withContext(Dispatchers.IO) {
            appDatabase.wordDao().clearWords()
            appDatabase.profileDao().clearProfile()
            appDatabase.homeDao().clearHomeSummary()
        }
    }
}
