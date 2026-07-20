package com.iti.linguaquest.core.preferences.data.datasource

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val TARGET_LANGUAGE = stringPreferencesKey("target_language")
    val PROFICIENCY_LEVEL = stringPreferencesKey("proficiency_level")
    val APP_THEME = stringPreferencesKey("app_theme")
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
}