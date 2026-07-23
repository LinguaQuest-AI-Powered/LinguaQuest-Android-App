package com.iti.linguaquest.core.cache.data.datasource

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val TARGET_LANGUAGE = intPreferencesKey("target_language")
    val TARGET_LANGUAGE_NAME = stringPreferencesKey("target_language_name")
    val NATIVE_LANGUAGE = intPreferencesKey("native_language")
    val NATIVE_LANGUAGE_NAME = stringPreferencesKey("native_language_name")
    val PROFICIENCY_LEVEL = stringPreferencesKey("proficiency_level")
    val APP_THEME = stringPreferencesKey("app_theme")
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
    val REMINDER_TIME = stringPreferencesKey("reminder_time")
    val REMINDER_DAYS = stringPreferencesKey("reminder_days")
}