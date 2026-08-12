package com.iti.linguaquest.core.tutorial.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.iti.linguaquest.core.di.UserSettingsDataStore
import com.iti.linguaquest.core.tutorial.data.TutorialPreferences
import com.iti.linguaquest.core.tutorial.domain.TutorialManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TutorialModule {

    @Provides
    @Singleton
    fun provideTutorialPreferences(
        @UserSettingsDataStore dataStore: DataStore<Preferences>
    ): TutorialPreferences {
        return TutorialPreferences(dataStore)
    }

    @Provides
    @Singleton
    fun provideTutorialManager(
        preferences: TutorialPreferences
    ): TutorialManager {
        return TutorialManager(preferences)
    }
}
