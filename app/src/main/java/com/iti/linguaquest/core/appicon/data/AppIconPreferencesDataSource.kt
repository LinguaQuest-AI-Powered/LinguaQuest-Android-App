package com.iti.linguaquest.core.appicon.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first


import com.iti.linguaquest.core.di.UserSettingsDataStore

class AppIconPreferencesDataSource @Inject constructor(
    @UserSettingsDataStore private val dataStore: DataStore<Preferences>
) {
    object Keys {
        val LAST_USER_INTERACTION_AT = longPreferencesKey("last_user_interaction_at")
        val OBSERVED_STREAK_DAYS = intPreferencesKey("observed_streak_days")
        val OBSERVED_ACHIEVEMENT_COUNT = intPreferencesKey("observed_achievement_count")
        val CONSUMED_ACHIEVEMENT_COUNT = intPreferencesKey("consumed_achievement_count")
    }

    suspend fun getPreferences(): Preferences = dataStore.data.first()

    suspend fun editPreferences(transform: MutablePreferences.() -> Unit) {
        dataStore.edit { preferences -> preferences.transform() }
    }
}
