package com.iti.linguaquest.core.preferences.cache

import androidx.datastore.preferences.core.stringPreferencesKey

object TokenKeys {
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    const val IS_LOGGED_IN = "is_logged_in"
    val AVATAR_URL = stringPreferencesKey("profile_avatar_url_cache")
}