package com.iti.linguaquest.core.cache.data.datasource

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object SessionKeys {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val FIRST_TIME = booleanPreferencesKey("first_time")
    val LAST_LOGGED_IN_USER_ID = intPreferencesKey("last_logged_in_user_id")
}
