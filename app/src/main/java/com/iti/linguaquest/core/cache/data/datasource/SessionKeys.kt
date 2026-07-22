package com.iti.linguaquest.core.cache.data.datasource

import androidx.datastore.preferences.core.booleanPreferencesKey

object SessionKeys {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val FIRST_TIME = booleanPreferencesKey("first_time")
}
