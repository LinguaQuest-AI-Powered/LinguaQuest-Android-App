package com.iti.linguaquest.core.tutorial.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.iti.linguaquest.core.di.UserSettingsDataStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TutorialPreferences(
    private val dataStore: DataStore<Preferences>
) {
    fun isTutorialCompleted(tourId: String): Flow<Boolean> {
        val key = booleanPreferencesKey(getCompletedKey(tourId))
        return dataStore.data.map { preferences ->
            preferences[key] ?: false
        }
    }

    suspend fun setTutorialCompleted(tourId: String, completed: Boolean) {
        val key = booleanPreferencesKey(getCompletedKey(tourId))
        dataStore.edit { preferences ->
            preferences[key] = completed
        }
    }

    suspend fun resetAllTutorials() {
        dataStore.edit { preferences ->
            val keysToRemove = preferences.asMap().keys.filter { 
                it.name.startsWith(TUTORIAL_PREFIX) 
            }
            keysToRemove.forEach { key ->
                preferences.remove(key)
            }
        }
    }

    private fun getCompletedKey(tourId: String): String = "$TUTORIAL_PREFIX$tourId"

    companion object {
        private const val TUTORIAL_PREFIX = "tutorial_completed_"
    }
}
