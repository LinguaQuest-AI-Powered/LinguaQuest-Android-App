package com.iti.linguaquest.core.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val TARGET_LANGUAGE = stringPreferencesKey("target_language")
    val NATIVE_LANGUAGE = stringPreferencesKey("native_language")
    val PROFICIENCY_LEVEL = stringPreferencesKey("proficiency_level")
    val APP_THEME = stringPreferencesKey("app_theme")
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
}