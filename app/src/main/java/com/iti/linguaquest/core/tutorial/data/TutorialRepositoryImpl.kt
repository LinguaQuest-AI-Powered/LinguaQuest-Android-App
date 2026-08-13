package com.iti.linguaquest.core.tutorial.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.iti.linguaquest.core.di.UserSettingsDataStore
import com.iti.linguaquest.core.tutorial.domain.repository.TutorialRepository
import com.iti.linguaquest.core.tutorial.model.TourId
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TutorialRepositoryImpl @Inject constructor(
    @UserSettingsDataStore private val dataStore: DataStore<Preferences>
) : TutorialRepository {

    override suspend fun isTourCompleted(tourId: TourId): Boolean {
        val key = booleanPreferencesKey(getCompletedKey(tourId.key))
        val preferences = dataStore.data.first()
        return preferences[key] ?: false
    }

    override suspend fun setTourCompleted(tourId: TourId, completed: Boolean) {
        val key = booleanPreferencesKey(getCompletedKey(tourId.key))
        dataStore.edit { preferences ->
            preferences[key] = completed
        }
    }

    override suspend fun resetAllTours() {
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
