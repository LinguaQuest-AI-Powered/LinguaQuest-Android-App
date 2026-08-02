package com.iti.linguaquest.core.appicon.data

import androidx.datastore.preferences.core.Preferences
import com.iti.linguaquest.core.appicon.domain.AppIconSnapshot
import com.iti.linguaquest.core.appicon.domain.AppIconStateRepository
import com.iti.linguaquest.core.appicon.util.AppIconClock
import javax.inject.Inject

class AppIconStateRepositoryImpl @Inject constructor(
    private val dataSource: AppIconPreferencesDataSource,
    private val clock: AppIconClock
) : AppIconStateRepository {

    override suspend fun snapshot(): AppIconSnapshot {
        return dataSource.getPreferences().toSnapshot()
    }

    override suspend fun markUserInteraction() {
        dataSource.editPreferences {
            this[AppIconPreferencesDataSource.Keys.LAST_USER_INTERACTION_AT] = clock.nowMillis()
        }
    }

    override suspend fun observeHomeSnapshot(streakDays: Int) {
        dataSource.editPreferences {
            this[AppIconPreferencesDataSource.Keys.OBSERVED_STREAK_DAYS] = streakDays
        }
    }

    override suspend fun observeProfileSnapshot(streakDays: Int, achievementCount: Int) {
        dataSource.editPreferences {
            this[AppIconPreferencesDataSource.Keys.OBSERVED_STREAK_DAYS] = streakDays
            this[AppIconPreferencesDataSource.Keys.OBSERVED_ACHIEVEMENT_COUNT] = achievementCount

            val consumed = this[AppIconPreferencesDataSource.Keys.CONSUMED_ACHIEVEMENT_COUNT] ?: 0
            if (consumed > achievementCount) {
                this[AppIconPreferencesDataSource.Keys.CONSUMED_ACHIEVEMENT_COUNT] = achievementCount
            }
        }
    }

    override suspend fun consumeAchievements(upToCount: Int) {
        dataSource.editPreferences {
            this[AppIconPreferencesDataSource.Keys.CONSUMED_ACHIEVEMENT_COUNT] = upToCount
        }
    }

    private fun Preferences.toSnapshot(): AppIconSnapshot = AppIconSnapshot(
        lastUserInteractionAtMillis = this[AppIconPreferencesDataSource.Keys.LAST_USER_INTERACTION_AT],
        observedStreakDays = this[AppIconPreferencesDataSource.Keys.OBSERVED_STREAK_DAYS] ?: 0,
        observedAchievementCount = this[AppIconPreferencesDataSource.Keys.OBSERVED_ACHIEVEMENT_COUNT] ?: 0,
        consumedAchievementCount = this[AppIconPreferencesDataSource.Keys.CONSUMED_ACHIEVEMENT_COUNT] ?: 0
    )
}
