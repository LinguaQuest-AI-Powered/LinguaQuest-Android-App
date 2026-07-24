package com.iti.linguaquest.core.cache.token

import androidx.datastore.preferences.core.stringPreferencesKey

object TokenKeys {
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"
    val AVATAR_URL = stringPreferencesKey("profile_avatar_url_cache")
}
