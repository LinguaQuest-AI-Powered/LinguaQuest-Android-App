package com.iti.linguaquest.core.appicon.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.iti.linguaquest.core.appicon.domain.AppIconClock
import com.iti.linguaquest.core.appicon.domain.AppIconSnapshot
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class AppIconStateRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val clock: AppIconClock
) : AppIconStateRepository {
    private object Keys {
        val LAST_USER_INTERACTION_AT = longPreferencesKey("last_user_interaction_at")
        val OBSERVED_STREAK_DAYS = intPreferencesKey("observed_streak_days")
        val OBSERVED_ACHIEVEMENT_COUNT = intPreferencesKey("observed_achievement_count")
        val CONSUMED_ACHIEVEMENT_COUNT = intPreferencesKey("consumed_achievement_count")
    }

    override suspend fun snapshot(): AppIconSnapshot {
        val preferences = dataStore.data.first()
        return preferences.toSnapshot()
    }

    override suspend fun markUserInteraction() {
        dataStore.edit { preferences ->
            preferences[Keys.LAST_USER_INTERACTION_AT] = clock.nowMillis()
        }
    }

    override suspend fun observeHomeSnapshot(streakDays: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.OBSERVED_STREAK_DAYS] = streakDays
        }
    }

    override suspend fun observeProfileSnapshot(streakDays: Int, achievementCount: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.OBSERVED_STREAK_DAYS] = streakDays
            preferences[Keys.OBSERVED_ACHIEVEMENT_COUNT] = achievementCount

            val consumed = preferences[Keys.CONSUMED_ACHIEVEMENT_COUNT] ?: 0
            if (consumed > achievementCount) {
                preferences[Keys.CONSUMED_ACHIEVEMENT_COUNT] = achievementCount
            }
        }
    }

    override suspend fun consumeAchievements(upToCount: Int) {
        dataStore.edit { preferences ->
            preferences[Keys.CONSUMED_ACHIEVEMENT_COUNT] = upToCount
        }
    }

    private fun Preferences.toSnapshot(): AppIconSnapshot = AppIconSnapshot(
        lastUserInteractionAtMillis = this[Keys.LAST_USER_INTERACTION_AT],
        observedStreakDays = this[Keys.OBSERVED_STREAK_DAYS] ?: 0,
        observedAchievementCount = this[Keys.OBSERVED_ACHIEVEMENT_COUNT] ?: 0,
        consumedAchievementCount = this[Keys.CONSUMED_ACHIEVEMENT_COUNT] ?: 0
    )
}
